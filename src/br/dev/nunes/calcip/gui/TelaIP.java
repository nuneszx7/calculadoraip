package br.dev.nunes.calcip.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import br.dev.nunes.calcip.model.EnderecoIP;

public class TelaIP extends JFrame {
    private JTextField campoIP;
    private JTextField campoSubCIDR;
    private JTextArea resultadoArea;

    private EnderecoIP endereco;

    public TelaIP() {
        super("Calculadora de IP - ConectaMais");

        campoIP = new JTextField();
        campoSubCIDR = new JTextField();

        JPanel painelEntrada = new JPanel(new GridLayout(3, 2, 5, 5));
        painelEntrada.add(new JLabel("Endereço IP com CIDR (ex: 192.168.0.0/24):"));
        painelEntrada.add(campoIP);
        painelEntrada.add(new JLabel("Nova CIDR para Sub-rede (opcional):"));
        painelEntrada.add(campoSubCIDR);

        JButton botaoCalcular = new JButton("Calcular");
        botaoCalcular.addActionListener(this::acaoCalcular);
        painelEntrada.add(botaoCalcular);

        JButton botaoSubrede = new JButton("Calcular Sub-rede");
        botaoSubrede.addActionListener(this::acaoSubrede);
        painelEntrada.add(botaoSubrede);

        resultadoArea = new JTextArea(10, 50);
        resultadoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        add(painelEntrada, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void acaoCalcular(ActionEvent e) {
        try {
            endereco = new EnderecoIP(campoIP.getText());
            StringBuilder sb = new StringBuilder();

            sb.append("IP: ").append(endereco.getIp()).append("\n");
            sb.append("CIDR: ").append(endereco.getCidr()).append("\n");
            sb.append("Classe: ").append(endereco.getClasse()).append("\n");
            sb.append("Máscara Decimal: ").append(endereco.getMascaraDecimal()).append("\n");
            sb.append("Máscara Binária: ").append(endereco.getMascaraBinaria()).append("\n");
            sb.append("Total de IPs disponíveis: ").append(endereco.getTotalIPs()).append("\n");

            resultadoArea.setText(sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoSubrede(ActionEvent e) {
        try {
            if (endereco == null) {
                throw new IllegalStateException("Calcule primeiro o IP principal.");
            }

            int novoCIDR = Integer.parseInt(campoSubCIDR.getText());
            EnderecoIP subrede = endereco.criarSubrede(novoCIDR);

            StringBuilder sb = new StringBuilder();
            sb.append("Sub-rede com CIDR /").append(novoCIDR).append(":\n");
            sb.append("Máscara Decimal: ").append(subrede.getMascaraDecimal()).append("\n");
            sb.append("Máscara Binária: ").append(subrede.getMascaraBinaria()).append("\n");
            sb.append("Total de IPs disponíveis: ").append(subrede.getTotalIPs()).append("\n");

            resultadoArea.append("\n" + sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao calcular sub-rede: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
