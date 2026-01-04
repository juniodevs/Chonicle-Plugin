# Chronicles

Chronicles é um plugin para servidores Minecraft (Spigot/Paper 1.20+) que permite rastrear e visualizar a história de itens no jogo. Com ele, os jogadores podem ver eventos passados relacionados a um item específico que estão segurando, como criação, trocas e modificações.

## 📋 Funcionalidades

- **Rastreamento de Itens**: Mantém um registro histórico detalhado de cada item rastreado.
- **Visualização em Livro**: Exibe o histórico do item em uma interface de livro amigável dentro do jogo.
- **Suporte a Idiomas**: Suporte nativo para Português (pt) e Inglês (en).
- **Armazenamento Local**: Utiliza SQLite para armazenamento leve e eficiente dos dados, sem necessidade de configuração de banco de dados externo.

## 🚀 Instalação

1. Baixe o arquivo `.jar` mais recente na aba de Releases (ou compile o projeto).
2. Coloque o arquivo na pasta `plugins` do seu servidor.
3. Reinicie o servidor.
4. O arquivo de configuração `config.yml` será gerado automaticamente na pasta `plugins/Chronicles/`.

## 🛠️ Configuração

O arquivo `config.yml` permite configurar o idioma do plugin.

```yaml
# Idioma do plugin (pt ou en)
language: pt
```

## 💻 Comandos

| Comando | Descrição | Permissão |
|---|---|---|
| `/history` | Mostra a história do item que está na sua mão principal. | `masterpl.history` |
| `/lore` | Alias para `/history`. | `masterpl.history` |
| `/chronicle` | Alias para `/history`. | `masterpl.history` |

## 🛡️ Permissões

- `masterpl.history`: Permite que o jogador use o comando `/history` para ver o histórico de um item. (Padrão: true)

## 🏗️ Como Compilar

Este projeto utiliza Maven para gerenciamento de dependências e construção.

### Pré-requisitos

- JDK 17 ou superior
- Maven instalado

### Passos

1. Clone este repositório:
   ```bash
   git clone https://github.com/seu-usuario/MasterPl.git
   ```
2. Navegue até a pasta do projeto:
   ```bash
   cd MasterPl
   ```
3. Compile o projeto:
   ```bash
   mvn clean package
   ```
4. O arquivo `.jar` compilado estará na pasta `target/` (ex: `Chronicles-1.0.jar`).

## 🤝 Contribuição

Este projeto é livre e aberto para todos! Toda ajuda é muito bem-vinda.

Se você quiser contribuir com código, corrigir bugs, sugerir novas funcionalidades ou **traduzir o plugin para outros idiomas**, sinta-se à vontade para abrir uma Issue ou enviar um Pull Request.

## 📝 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
