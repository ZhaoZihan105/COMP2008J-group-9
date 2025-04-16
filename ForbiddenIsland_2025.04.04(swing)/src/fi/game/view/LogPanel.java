package fi.game.view;

import javax.swing.*;
import java.awt.*;

/**
 * 日志面板，用于显示游戏过程中的消息
 */
public class LogPanel extends JPanel {
    private JTextArea logArea;
    private JScrollPane scrollPane;

    public LogPanel() {
        setPreferredSize(new Dimension(250, 150));
        setBorder(BorderFactory.createTitledBorder("游戏日志"));
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 添加日志
     */
    public void addLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    /**
     * 清除日志
     */
    public void clearLog() {
        logArea.setText("");
    }
}