package com.magnet.plugin.project;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.magnet.plugin.helpers.ProjectHelper;
import com.magnet.plugin.helpers.VerifyHelper;
import com.magnet.tools.cli.simple.SimpleGenConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * manage access to project files and information
 */
public class ProjectManager {
    private static final String MANIFEST_FILE = "AndroidManifest.xml";
    private static final String PACKAGE_LINE_PREFIX = "package=";
    private static final String BUILD_PREFIX = "build";

    public static String getResourceFolder(Project project) {
        String result;
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        if ((result = getGradle(vFiles, "res")) == null) {
            result = getNonGradle(vFiles, "res");
        }
        return result;
    }

    public static File getResourceFolderFile(Project project) {
        return new File(getResourceFolder(project));
    }

    public static String getSourceFolder(Project project) {
        String result;
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        if ((result = getGradle(vFiles, "java")) == null) {
            result = getNonGradle(vFiles, "src");
        }

        return result;
    }

    public static File getSourceFolderFile(Project project) {
        return new File(getSourceFolder(project));
    }

    private static String getGradle(VirtualFile[] vFiles, String folder) {
        String result = null;
        for (VirtualFile file : vFiles) {
            String filePath = file.getCanonicalPath();
            String[] path = filePath.split("/");
            String template = "";

            if (path.length >= 2) {
                template = path[path.length - 2] + File.separator + path[path.length - 1];
            }

            if (template.contains("main" + File.separator + folder)) {
                result = filePath;
                break;
            }
        }
        return result;
    }

    private static String getNonGradle(VirtualFile[] vFiles, String folder) {
        String result = null;
        for (VirtualFile file : vFiles) {
            String filePath = file.getCanonicalPath();
            String[] path = filePath.split("/");
            String template = "";

            if (path.length >= 1) {
                template = path[path.length - 1];
            }

            if (template.contains(folder)) {
                result = filePath;
                break;
            }
        }
        return result;
    }

    private static String getManifestPath(Project project) {
        Module module = ProjectHelper.getMainModule(project);
        String path = ProjectHelper.getModulePath(module, project);
        File manifest = findFileWithBuildExclude(new File(path), MANIFEST_FILE);
        return manifest.getAbsolutePath();
    }

    private static File findFileWithBuildExclude(File aFile, String toFind) {
        String path = aFile.getPath();
        if (aFile.isFile() &&
                aFile.getName().contains(toFind) && (!path.contains(BUILD_PREFIX))) {
            return aFile;
        } else if (aFile.isDirectory()) {
            for (File child : aFile.listFiles()) {
                File found = findFileWithBuildExclude(child, toFind);
                if (found != null) {
                    return found;
                }//if
            }//for
        }//else
        return null;
    }//met

    public static String getPackageName(Project project) {
        String packageName = "";
        try {
            String filePath = getManifestPath(project);
            if (filePath != null) {
                BufferedReader reader =
                        new BufferedReader(new FileReader(filePath));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    line = line.replaceAll(" ", "");
                    if (line.startsWith(PACKAGE_LINE_PREFIX)) {
                        packageName = line.replace(PACKAGE_LINE_PREFIX, "");
                        packageName = packageName.replaceAll("\"", "");
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }
//        Logger.info(FileHelper.class, packageName);
        packageName = VerifyHelper.verifyPackageName(packageName);
        return packageName;
    }

    public static File getClassFile(Project project, String packageName, String controllerName) {
        return new File(getSourceFolder(project), (packageName + "." + SimpleGenConstants.CONTROLLER_API_SUB_PACKAGE).replaceAll("\\.", "/") + "/" + controllerName + ".java");
    }
}
