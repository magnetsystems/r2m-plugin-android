///*
// * Copyright (c) 2014 Magnet Systems, Inc.
// * All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you
// * may not use this file except in compliance with the License. You
// * may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// * implied. See the License for the specific language governing
// * permissions and limitations under the License.
// */
//
package legacy;
//
//import javax.swing.*;
//
//public class CustomFrame extends JFrame {
//
//    private MethodNameSection panel;
//    private MethodTypeSection type;
//    private HeaderSection header;
//    private RequestPayloadSection payload;
//    private ResponseSection response;
//    private ButtonsSection buttons;
//
//    {
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        panel = new MethodNameSection();
//        header = new HeaderSection();
//        payload = new RequestPayloadSection();
//        type = new MethodTypeSection(payload);
//        response = new ResponseSection();
//        buttons = new ButtonsSection();
//
//        GroupLayout layout = new GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                .addComponent(panel)
//                .addComponent(type)
//                .addComponent(header)
//                .addComponent(payload)
//                .addComponent(response)
//                .addComponent(buttons, GroupLayout.Alignment.CENTER));
//        layout.setVerticalGroup(
//                layout.createSequentialGroup()
//                .addComponent(panel)
//                .addComponent(type)
//                .addComponent(header)
//                .addComponent(payload)
//                .addComponent(response)
//                .addComponent(buttons));
//        pack();
//    }
//
//    public static void main(String args[]) {
//
//        try {
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(CustomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CustomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CustomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CustomFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new CustomFrame().setVisible(true);
//            }
//        });
//    }
//}
