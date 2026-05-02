# CLI Currency Converter 💱

Um conversor de moedas interativo direto do seu terminal, desenvolvido em Java. Este projeto fornece uma interface gráfica de terminal, permitindo conversões de moedas em tempo real com performance graças ao uso de cache local.


[![asciicast](https://asciinema.org/a/mcj3GmdGwevwHirs.svg)](https://asciinema.org/a/mcj3GmdGwevwHirs)

---
## ✨ Funcionalidades

* **Interface Gráfica no Terminal (TUI):** Navegação fluida e intuitiva utilizando a biblioteca Lanterna.
* **Conversão em Tempo Real:** Integração com a [ExchangeRate-API](https://www.exchangerate-api.com/) para taxas de câmbio atualizadas.
* **Gerenciamento de API Key:** Sistema para que os usuários possam inserir e salvar sua própria chave de API, gerenciado via Jackson.
* **Alta Performance (Cache LRU):** Implementação de um Cache LRU (Least Recently Used) para evitar requisições redundantes à API, economizando limite de uso e acelerando respostas repetidas.
* **Fácil Instalação:** Distribuído como um pacote `.rpm` para instalação nativa em distribuições Linux compatíveis.

---
## 🛠️ Tecnologias Utilizadas

* **[Java](https://www.java.com/):** Linguagem principal do projeto.
* **[Lanterna](https://github.com/mabe02/lanterna):** Para a construção da interface gráfica no terminal.
* **[Jackson](https://github.com/FasterXML/jackson):** Para serialização/deserialização das respostas JSON da API e persistência das configurações do usuário.
* **[ExchangeRate-API](https://www.exchangerate-api.com/):** Fornecimento das taxas de câmbio globais.
* **Estrutura de Dados:** Cache LRU customizado.

---
## 🚀 Como Instalar

### Via pacote RPM (Red Hat, Fedora, CentOS, openSUSE)
Se você estiver utilizando uma distribuição baseada em RPM, baixe a versão mais recente na aba [Releases](https://github.com/JoseCarlos67/CLI-Currency_Converter/releases) e execute:

```bash
sudo rpm -i cli-currency-converter-<versao>.rpm
```

---
## ⚙️ Configuração (API Key)
Para utilizar o conversor, você precisará de uma chave da ExchangeRate-API.

1. Crie uma conta gratuita em [ExchangeRate-API](https://www.exchangerate-api.com/).

2. Ao abrir o CLI Currency Converter pela primeira vez, digite a sua chave no campo adequado.

3. Você pode salvar a chave para não precisar digitá-la novamente nas próximas execuções.

---
### 📄 Licença

Distribuído sob a licença MIT. Veja o ficheiro LICENSE para mais detalhes.