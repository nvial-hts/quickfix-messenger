package com.jramoyo.qfixmessenger.ui.listeners;

import com.jramoyo.qfixmessenger.ui.QFixMessengerFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author nvial
 */
public class SessionSetSeqNumActionListener implements ActionListener
{
    private static final Logger logger = LoggerFactory.getLogger(SessionSetSeqNumActionListener.class);

    private QFixMessengerFrame frame;
    private Session session;

    public SessionSetSeqNumActionListener(QFixMessengerFrame frame, Session session)
    {
        this.frame = frame;
        this.session = session;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (session == null) {
            JOptionPane.showMessageDialog(frame, "Null FIX Session.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupération des seqnums actuels
        int currentSender = session.getExpectedSenderNum();
        int currentTarget = session.getExpectedTargetNum();

        // === Dialog compact ===
        JDialog dialog = new JDialog(frame, "Force FIX Sequence Numbers", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(300, 160);
        dialog.setLocationRelativeTo(frame);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel senderLabel = new JLabel("SenderSeqNum :");
        JTextField senderField = new JTextField(6);
        senderField.setText(String.valueOf(currentSender));

        JLabel targetLabel = new JLabel("TargetSeqNum :");
        JTextField targetField = new JTextField(6);
        targetField.setText(String.valueOf(currentTarget));

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(senderLabel, gbc);
        gbc.gridx = 1;             inputPanel.add(senderField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(targetLabel, gbc);
        gbc.gridx = 1;             inputPanel.add(targetField, gbc);

        JPanel btnPanel = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        btnPanel.add(ok);
        btnPanel.add(cancel);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // === Validation ===
        ok.addActionListener(ev -> {
            try {
                int senderSeq = Integer.parseInt(senderField.getText().trim());
                int targetSeq = Integer.parseInt(targetField.getText().trim());

                if (senderSeq < 1 || targetSeq < 1) {
                    JOptionPane.showMessageDialog(dialog,
                            "Sequence numbers must be >= 1.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // === Forçage QuickFIX/J ===
                logger.info("Before {}", session);
                session.setNextSenderMsgSeqNum(senderSeq);
                session.setNextTargetMsgSeqNum(targetSeq);
                logger.info("After {}", session);

                JOptionPane.showMessageDialog(dialog,
                        "Sequence Numbers changed with success.",
                        "OK", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Please enter valid integers.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancel.addActionListener(ev -> dialog.dispose());

        dialog.setVisible(true);
    }
}
