# 🦷 OdontoCare: Sistema de Gerenciamento Odontológico

## 🔍 Visão Geral

OdontoCare é um sistema completo de gerenciamento para clínicas odontológicas, desenvolvido com Spring Boot e Java. O sistema permite o gerenciamento de pacientes, dentistas, consultas, além de incluir um módulo avançado de análise de sinistros usando IA para prever e reduzir riscos relacionados a atendimentos odontológicos.

## ✨ Características

- **Gestão de Pacientes**: Cadastro, edição e visualização de dados dos pacientes.  
- **Gestão de Dentistas**: Gerenciamento de profissionais, especialidades e disponibilidade.  
- **Gestão de Consultas**: Agendamento, confirmação, cancelamento e histórico de consultas.  
- **Internacionalização**: Suporte a múltiplos idiomas (português e inglês).  
- **Sistema de Notificações**: Envio automatizado de notificações por e-mail para confirmação e lembretes de consultas.  
- **Dashboard**: Visão consolidada de indicadores e atividades da clínica.  
- **Análise de Sinistros com IA**: Sistema preditivo para identificação de possíveis sinistros com base no histórico de pacientes.  
- **Segurança**: Autenticação de usuários, controle de acesso por perfil.  
- **Monitoramento**: Endpoints Actuator para monitoramento de saúde, métricas e logs do sistema.  

## 🔧 Tecnologias Utilizadas

- **Backend**: Java 17, Spring Boot 3.2  
- **Frontend**: Thymeleaf, CSS3, 
- **Banco de Dados**: Oracle Database  
- **Segurança**: Spring Security  
- **Persistência**: JPA/Hibernate  
- **Mensageria**: ActiveMQ (para notificações)  
- **Monitoramento**: Spring Boot Actuator  
- **IA**: Integração com OpenAI para análise preditiva  
- **Internacionalização**: ResourceBundle e Mensagens Localizadas

## 📩 Configuração de recursos de mensageria

O OdontoCare implementa um sistema robusto de notificações baseado no padrão de mensageria produtor/consumidor utilizando Apache ActiveMQ. A configuração está estruturada em:

JmsConfig.java: Configura o broker ActiveMQ, JmsTemplate e o container de listener
NotificacaoService.java: Atua como produtor, enviando mensagens para a fila "fila.notificacoes"
NotificacaoConsumer.java: Consumidor que processa as mensagens recebidas da fila
NotificacaoDTO.java: Estrutura de dados para as notificações

Este sistema permite o envio assíncrono de notificações por e-mail para confirmação, atualização e cancelamento de consultas, sem bloquear o fluxo principal da aplicação. Cada operação relevante do ConsultaService gera automaticamente as notificações apropriadas, que são processadas de forma independente pelo consumidor.

## 🔒 Sistema de Segurança e Perfis de Usuário

O **OdontoCare** implementa um robusto sistema de segurança baseado em **Spring Security**, com autenticação, autorização e controle de acesso por **perfis de usuário**. O sistema possui três níveis de acesso pré-configurados:

### Perfis de Usuário e Credenciais Padrão

| Perfil       | Usuário         | Senha             | Nível de Acesso                                                                                                                                 |
|--------------|------------------|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| Administrador| `admin`          | `admin123`        | Acesso total ao sistema, incluindo gerenciamento de usuários, configurações, métricas e todos os módulos operacionais                          |
| Dentista     | `dentista`       | `dentista123`     | Acesso ao dashboard, gerenciamento de consultas, visualização e consulta do módulo de IA                                                       |
| Recepcionista| `recepcionista`  | `recepcionista123`| Acesso ao gerenciamento de pacientes, agendamento de consultas e operações básicas do sistema                                                  |

### Recursos de Segurança

- ✅ **Autenticação baseada em formulário**: Sistema de login seguro com proteção contra ataques de força bruta  
- 🔐 **Autorização baseada em roles**: Controle granular de acesso a funcionalidades específicas  
- ⏳ **Sessões gerenciadas**: Timeout automático de sessões inativas  
- 🔒 **Criptografia de senhas**: Utilização de `BCrypt` para armazenamento seguro de credenciais  
- 🛡️ **Proteção CSRF**: Prevenção contra ataques *Cross-Site Request Forgery*  
- ⚠️ **Páginas de erro personalizadas**: Tratamento adequado de erros de autenticação e autorização  


## 🤖 Prevenção de Sinistros com IA

Um dos diferenciais do **OdontoCare** é o módulo de prevenção de sinistros baseado em IA, que analisa os padrões de consulta dos pacientes e identifica possíveis riscos de problemas odontológicos graves ou reclamações. Este sistema classifica os pacientes em três níveis de risco:

- **Alto Risco (6+ consultas)**: Pacientes com histórico extenso de consultas que podem indicar problemas crônicos, tratamentos ineficazes ou potenciais litígios.  
- **Médio Risco (3-5 consultas)**: Pacientes que demandam atenção e acompanhamento regular.  
- **Baixo Risco (1-2 consultas)**: Pacientes com baixa probabilidade de sinistros.  

O sistema utiliza a API da OpenAI para:

- Analisar o histórico de consultas dos pacientes  
- Gerar sugestões de tratamentos preventivos  
- Oferecer insights para melhorar a abordagem clínica  
- Analisar sintomas relatados para detecção precoce de problemas  

Este módulo representa uma inovação significativa na gestão de riscos em clínicas odontológicas, permitindo a tomada de decisões proativas para mitigar problemas antes que escalem.

## 📂 Estrutura do Projeto

```text
odonto-care
├── src
│   └── main
│       ├── java
│       │   └── com.fiap.odontocare
│       │       ├── actuator        - Configurações de métricas e health checks
│       │       ├── config          - Configurações do Spring (segurança, i18n, etc.)
│       │       ├── controller      - Controladores REST e Web
│       │       ├── dto             - Objetos de Transferência de Dados
│       │       ├── entity          - Entidades JPA
│       │       ├── exception       - Manipulação de erros personalizados
│       │       ├── repository      - Interfaces JPA
│       │       └── service         - Lógica de negócio
│       └── resources
│           ├── static             - Recursos estáticos (CSS, imagens)
│           ├── templates
│           │   ├── fragments      - Fragments Thymeleaf reutilizáveis
│           │   ├── ia             - Páginas do módulo de IA
│           │   └── pacientes      - Páginas de gestão de pacientes
│           ├── application.properties            - Configurações da aplicação
│           ├── messages.properties               - Mensagens padrão
│           ├── messages_pt_BR.properties         - Mensagens em português
│           └── messages_en.properties            - Mensagens em inglês
├── pom.xml                    - Configuração do Maven
└── README.md                  - Documentação do projeto
```

## 📡 Endpoints

### 🔹 Endpoints Principais

| Método | Caminho                       | Descrição                                      |
|--------|-------------------------------|------------------------------------------------|
| GET    | `/`                           | Página inicial / Dashboard                     |
| GET    | `/login`                      | Página de login                                |
| GET    | `/dashboard`                  | Dashboard administrativo                       |
| GET    | `/pacientes`                  | Listagem e gestão de pacientes                 |
| GET    | `/consultasView`             | Visualização e gerenciamento de consultas      |
| GET    | `/ia/dashboard`               | Dashboard com análises de IA                   |
| GET    | `/ia/analise/{pacienteId}`   | Análise preditiva de sinistro por paciente     |
| GET    | `/gerenciamento`             | Página de monitoramento do sistema             |

---

### 🔧 Endpoints Actuator (Monitoramento)

| Método | Caminho             | Descrição                                     |
|--------|---------------------|-----------------------------------------------|
| GET    | `/actuator/health`  | Verificação de saúde do sistema               |
| GET    | `/actuator/metrics` | Métricas do sistema                           |
| GET    | `/actuator/info`    | Informações da aplicação                      |
| GET    | `/actuator/env`     | Variáveis de ambiente                         |

### 📷 Screenshots

###  **tela de Login**  
![Login](https://github.com/bia98silva/OdontoCare_sprint4/blob/main/img/Login.png)

### 📊 **Dashboard Principal**   
Dashboard principal com indicadores da clínica
![Dashboard](https://github.com/bia98silva/OdontoCare_sprint4/blob/main/img/dash.PNG)

### 📊 **Dashboar De Analise com IA **  
Dashboard de análise de sinistros com IA
![DashboardAI](https://github.com/bia98silva/OdontoCare_sprint4/blob/main/img/dashIA.PNG)

Análise detalhada de paciente com recomendações de IA
![Dashboard](https://github.com/bia98silva/OdontoCare_sprint4/blob/main/img/Dashaprofundado.PNG)

###  **Diagrama do projeto**
![diagrama](https://github.com/bia98silva/OdontoCare_sprint4/blob/main/img/Devops.drawio.png)

### Link do video: https://youtu.be/slEiC_k-vsY

### Pré-requisitos

- JDK 17 ou superior  
- Maven 3.6 ou superior  
- Oracle Database  
- ActiveMQ (opcional para sistema de notificações)  

### Passos

```bash
# Clone o repositório:
git clone https://github.com/seu-usuario/odonto-care.git
cd odonto-care

# Configure o banco de dados no arquivo application.properties.

# Compile e execute o projeto:
mvn clean install
mvn spring-boot:run

### Grupo:
Nome: Beatriz Silva RM552600
Vitor Onofre Ramos RM553241
Pedro Henrique soares araujo - RM553801

