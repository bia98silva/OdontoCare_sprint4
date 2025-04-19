package com.Fiap.OdontoCare.service;

import com.Fiap.OdontoCare.entity.Consulta;
import com.Fiap.OdontoCare.entity.Paciente;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Service
public class IAService {

    @Value("${spring.ai.openai.api-key:dummy-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String gerarSugestoesTratamento(Paciente paciente, List<Consulta> consultasAnteriores) {
        String idade = "N/A";
        if (paciente.getDataNascimento() != null) {
            idade = String.valueOf(ChronoUnit.YEARS.between(
                    paciente.getDataNascimento().toLocalDate(), LocalDate.now()));
        }

        StringBuilder historico = new StringBuilder();
        if (consultasAnteriores != null && !consultasAnteriores.isEmpty()) {
            for (Consulta consulta : consultasAnteriores) {
                historico.append("- Data: ").append(consulta.getDataConsulta())
                        .append(", Status: ").append(consulta.getStatus());

                if (consulta.getDetalhes() != null && !consulta.getDetalhes().isEmpty()) {
                    historico.append(", Detalhes: ").append(consulta.getDetalhes());
                }

                historico.append("\n");
            }
        } else {
            historico.append("Sem histórico de consultas anteriores.");
        }

        String prompt = "Você é um assistente odontológico especializado. Com base nos dados do paciente e histórico de consultas, " +
                "sugira possíveis tratamentos ou cuidados preventivos.\n\n" +
                "Dados do Paciente:\n" +
                "- Nome: " + paciente.getNome() + "\n" +
                "- Idade: " + idade + " anos\n\n" +
                "Histórico de Consultas:\n" + historico.toString() + "\n\n" +
                "Por favor, forneça até 3 sugestões de tratamentos ou cuidados preventivos, explicando brevemente cada um. " +
                "Mantenha a resposta profissional e concisa.";

        try {
            return enviarParaOpenAI(prompt);
        } catch (Exception e) {
            return simulacaoGerarSugestoesTratamento(paciente, idade);
        }
    }

    public String gerarAnaliseOdontologica(String descricaoSintomas) {
        String prompt = "Você é um assistente odontológico especializado. Com base na descrição dos sintomas fornecida, " +
                "forneça uma análise preliminar e possíveis causas, lembrando que isto não substitui uma consulta profissional.\n\n" +
                "Sintomas descritos:\n" + descricaoSintomas + "\n\n" +
                "Por favor, forneça:\n" +
                "1. Uma análise preliminar dos possíveis problemas\n" +
                "2. Sugestões de cuidados imediatos que o paciente pode tomar\n" +
                "3. Uma recomendação sobre a urgência de consultar um dentista\n\n" +
                "Seja profissional, informativo e conciso. Deixe claro que esta é apenas uma orientação preliminar " +
                "e não substitui a avaliação de um profissional.";

        try {
            return enviarParaOpenAI(prompt);
        } catch (Exception e) {
            return simulacaoGerarAnaliseOdontologica(descricaoSintomas);
        }
    }


    private String enviarParaOpenAI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(OPENAI_API_URL, request, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> responseMessage = (Map<String, Object>) choice.get("message");
            return (String) responseMessage.get("content");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar a API da OpenAI: " + e.getMessage(), e);
        }
    }

    // Métodos de simulação para fallback em caso de erros
    private String simulacaoGerarSugestoesTratamento(Paciente paciente, String idade) {
        StringBuilder resposta = new StringBuilder();
        resposta.append("Com base na análise dos dados do paciente ").append(paciente.getNome());
        resposta.append(", ").append(idade).append(" anos, e seu histórico de consultas, sugiro:\n\n");

        resposta.append("1. **Acompanhamento Preventivo Regular**: ");
        resposta.append("Recomendo consultas de manutenção a cada 6 meses para limpeza profissional e avaliação geral. ");
        resposta.append("Isso ajuda a prevenir cáries e doenças periodontais.\n\n");

        resposta.append("2. **Higiene Oral Aprimorada**: ");
        resposta.append("Utilize escova de cerdas macias, fio dental diariamente e considere um enxaguante bucal com flúor. ");
        resposta.append("A escovação deve ser realizada após as refeições, com técnica adequada.\n\n");

        resposta.append("3. **Avaliação Radiográfica**: ");
        resposta.append("Sugiro realizar radiografias panorâmicas a cada 2 anos para detectar problemas ");
        resposta.append("que não são visíveis durante o exame clínico, como cáries interproximais ou lesões ósseas.\n\n");

        resposta.append("Esta é uma análise preliminar. Consulte seu dentista para um plano de tratamento personalizado.");

        return resposta.toString();
    }

    private String simulacaoGerarAnaliseOdontologica(String descricaoSintomas) {
        StringBuilder resposta = new StringBuilder();

        resposta.append("## Análise Preliminar\n\n");

        if (descricaoSintomas.toLowerCase().contains("dor")) {
            resposta.append("**Possíveis problemas**: A dor que você descreve pode indicar cárie dentária, ");
            resposta.append("inflamação pulpar, abscesso, sensibilidade ou problemas na gengiva. A localização e ");
            resposta.append("intensidade da dor são importantes para determinar a causa exata.\n\n");
        } else if (descricaoSintomas.toLowerCase().contains("sangramento")) {
            resposta.append("**Possíveis problemas**: O sangramento gengival geralmente indica gengivite ou ");
            resposta.append("doença periodontal. Também pode ser causado por escovação agressiva, ");
            resposta.append("uso inadequado do fio dental ou problemas sistêmicos.\n\n");
        } else {
            resposta.append("**Possíveis problemas**: Com base nos sintomas descritos, poderia se tratar de uma ");
            resposta.append("condição como sensibilidade dentária, problema oclusal (mordida desalinhada), ");
            resposta.append("ou desgaste do esmalte dentário.\n\n");
        }

        resposta.append("## Cuidados Imediatos\n\n");
        resposta.append("1. **Analgésicos de venda livre**: Se houver dor, um analgésico como ibuprofeno pode ajudar temporariamente.\n");
        resposta.append("2. **Evite extremos de temperatura**: Alimentos/bebidas muito quentes ou frios podem agravar a sensibilidade.\n");
        resposta.append("3. **Higiene cuidadosa**: Continue escovando e passando fio dental, mas com delicadeza na área afetada.\n\n");

        resposta.append("## Urgência da Consulta\n\n");
        resposta.append("Baseado nos sintomas descritos, sugiro **agendar uma consulta nos próximos 3-5 dias**. ");
        resposta.append("Se a dor for intensa, persistente, ou acompanhada de inchaço ou febre, ");
        resposta.append("procure atendimento odontológico de emergência imediatamente.\n\n");

        resposta.append("**AVISO**: Esta é apenas uma orientação preliminar e não substitui a avaliação presencial ");
        resposta.append("por um profissional de odontologia qualificado.");

        return resposta.toString();
    }
}