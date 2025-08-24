package com.ufs.ReservaAerea.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArquivoService {

    // Salva dados em um arquivo específico
    public void salvarDados(String nomeArquivo, String dados, boolean append) { 
        try (FileWriter writer = new FileWriter(nomeArquivo, append); 
                BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            bufferedWriter.write(dados); // Escreve os dados no arquivo
            bufferedWriter.newLine(); // Adiciona uma nova linha

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados em " + nomeArquivo + ": " + e.getMessage());
        }
    }

    // Lê todos os dados de um arquivo específico
    public List<String> lerTodosDados(String nomeArquivo) { 
        List<String> dados = new ArrayList<>(); // Lista para armazenar os dados lidos
        File arquivo = new File(nomeArquivo);  

        if (!arquivo.exists()) {
            return dados; // Retorna lista vazia se o arquivo não existir
        }

        try (FileReader reader = new FileReader(nomeArquivo);
                BufferedReader bufferedReader = new BufferedReader(reader)) { 

            String linha;
            while ((linha = bufferedReader.readLine()) != null) { // a cada linha do arquivo
                if (!linha.trim().isEmpty()) { // Ignora linhas vazias
                    dados.add(linha); // Adiciona a linha à lista de dados
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler dados de " + nomeArquivo + ": " + e.getMessage());
        }

        return dados; //retorna lista de Strings com os dados do arquivo
    }

    // Limpa um arquivo específico
    public void limparArquivo(String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write(""); // Limpa o arquivo
        } catch (IOException e) {
            System.err.println("Erro ao limpar arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }
    
}
