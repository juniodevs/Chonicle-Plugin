# ⚔️ Chronicles

[![SpigotMC](https://img.shields.io/badge/SpigotMC-Download-orange?style=for-the-badge&logo=spigotmc)](https://www.spigotmc.org/resources/chronicles.131460/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

> [!NOTE]
> Languages: [English](README.md) | [Português](README_PT.md)

**Todo Item Tem uma História.**

Chronicles dá vida aos itens do seu servidor gravando automaticamente sua história. Desde o momento em que uma espada é forjada até as batalhas lendárias que ela luta, cada evento significativo é rastreado e armazenado.

### 🎥 Review em Vídeo

[![Video Review](https://img.youtube.com/vi/5z8jFEyG_eo/0.jpg)](https://youtu.be/5z8jFEyG_eo)

## ✨ Funcionalidades

- **Rastreamento Automático:** Nenhuma entrada manual necessária. O plugin escuta eventos e atualiza a história do item automaticamente.
- **História Detalhada:** Rastreia criação, trocas, encantamentos, renomeações, reparos, mortes de chefes/jogadores, bloqueios de escudo e mudanças de dono.
- **Itens Suportados:** Funciona com todas as Espadas, Machados, Arcos, Bestas, Tridentes, Escudos, Élitros, Armaduras e Ferramentas.
- **Suporte Multi-Linguagem:** Vem com localização em Inglês e Português (PT-BR).
- **Armazenamento Local:** Utiliza SQLite para armazenamento leve e eficiente.

## 🚀 Instalação

1. Baixe o arquivo `.jar` mais recente no [SpigotMC](https://www.spigotmc.org/resources/chronicles.131460/) ou na aba de Releases.
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
   git clone https://github.com/juniodevs/Chonicle-Plugin.git
   ```
2. Navegue até a pasta do projeto:
   ```bash
   cd Chonicle-Plugin
   ```
3. Compile o projeto:
   ```bash
   mvn clean package
   ```
4. O arquivo `.jar` compilado estará na pasta `target/` (ex: `Chronicles-1.0-SNAPSHOT.jar`).

## 🤝 Contribuição

Este projeto é livre e aberto para todos! Toda ajuda é muito bem-vinda.

Se você quiser contribuir com código, corrigir bugs, sugerir novas funcionalidades ou **traduzir o plugin para outros idiomas**, sinta-se à vontade para abrir uma Issue ou enviar um Pull Request.

## 📝 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
