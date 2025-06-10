package br.dev.nunes.calcip.model;

import java.util.Arrays;

public class EnderecoIP {
    private final String ip;
    private final int cidr;

    public EnderecoIP(String entrada) {
        if (!entrada.contains("/")) {
            throw new IllegalArgumentException("Formato inválido. Use: IP/CIDR");
        }
        String[] partes = entrada.split("/");
        this.ip = partes[0];
        this.cidr = Integer.parseInt(partes[1]);
        validarIP(this.ip);
        if (cidr < 0 || cidr > 32) {
            throw new IllegalArgumentException("CIDR inválido.");
        }
    }

    public String getIp() {
        return ip;
    }

    public int getCidr() {
        return cidr;
    }

    public String getClasse() {
        int primeiroOcteto = Integer.parseInt(ip.split("\\.")[0]);
        if (primeiroOcteto >= 1 && primeiroOcteto <= 126) return "A";
        else if (primeiroOcteto >= 128 && primeiroOcteto <= 191) return "B";
        else if (primeiroOcteto >= 192 && primeiroOcteto <= 223) return "C";
        else return "Desconhecida";
    }

    public String getMascaraDecimal() {
        int mascara = -1 << (32 - cidr);
        return converterParaDecimal(mascara);
    }

    public String getMascaraBinaria() {
        int mascara = -1 << (32 - cidr);
        return String.format("%32s", Integer.toBinaryString(mascara)).replace(' ', '0')
                .replaceAll("(.{8})(?!$)", "$1.");
    }

    public int getTotalIPs() {
        return (cidr == 32) ? 1 : (int) Math.pow(2, 32 - cidr) - 2;
    }

    public EnderecoIP criarSubrede(int novoCIDR) {
        if (novoCIDR <= this.cidr || novoCIDR > 32) {
            throw new IllegalArgumentException("Novo CIDR inválido.");
        }
        return new EnderecoIP(this.ip + "/" + novoCIDR);
    }

    private String converterParaDecimal(int mascara) {
        return String.format("%d.%d.%d.%d",
                (mascara >>> 24) & 0xFF,
                (mascara >>> 16) & 0xFF,
                (mascara >>> 8) & 0xFF,
                mascara & 0xFF);
    }

    private void validarIP(String ip) {
        String[] octetos = ip.split("\\.");
        if (octetos.length != 4) {
            throw new IllegalArgumentException("IP inválido.");
        }

        for (String octeto : octetos) {
            int val = Integer.parseInt(octeto);
            if (val < 0 || val > 255) {
                throw new IllegalArgumentException("IP inválido.");
            }
        }
    }
    
    
    
    
}
