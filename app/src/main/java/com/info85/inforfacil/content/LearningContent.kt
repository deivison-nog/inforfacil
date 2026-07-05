package com.info85.inforfacil.content

import com.info85.inforfacil.R

enum class ActivityType {
    MULTIPLE_CHOICE,
    DRAG_AND_DROP,
    VISUAL_IDENTIFICATION
}

data class TopicItem(
    val title: String,
    val description: String,
    val iconResId: Int
)

data class PracticeActivityItem(
    val id: String,
    val type: ActivityType,
    val question: String,
    val options: List<String>,
    val correctOption: String? = null,
    val hint: String,
    val visualOptionIcons: Map<String, Int> = emptyMap(),
    val dragTargets: List<String> = emptyList(),
    val dragCorrectMapping: Map<String, String> = emptyMap()
)

data class ModuleContent(
    val moduleId: String,
    val title: String,
    val topics: List<TopicItem>,
    val activities: List<PracticeActivityItem>
)

object LearningContentProvider {

    fun getModuleContent(moduleId: String): ModuleContent {
        return allModules()[moduleId] ?: allModules().getValue("hardware")
    }

    fun allModules(): Map<String, ModuleContent> = mapOf(
        "hardware" to module(
            moduleId = "hardware",
            title = "Hardware",
            topics = listOf(
                topic("O que é Hardware", "Hardware são as partes físicas do computador que você pode tocar.", R.drawable.ic_hardware),
                topic("Componentes principais", "CPU, monitor, teclado e mouse formam a base de uso diário.", R.drawable.ic_hardware),
                topic("Armazenamento", "HD e SSD guardam seus arquivos e programas.", R.drawable.ic_arquivos)
            ),
            activities = listOf(
                dragActivity(
                    "hardware_drag",
                    "Arraste cada item para a categoria correta.",
                    mapOf("CPU" to "Componente interno", "Teclado" to "Periférico", "Mouse" to "Periférico", "SSD" to "Componente interno"),
                    listOf("Componente interno", "Periférico"),
                    "Pense no que fica dentro do gabinete e no que você usa com as mãos."
                ),
                mc("hardware_mc", "Qual é a função do processador (CPU)?", listOf("Exibir imagens", "Armazenar arquivos", "Executar instruções", "Fornecer energia"), "Executar instruções", "A CPU é o cérebro do computador."),
                visual(
                    "hardware_visual",
                    "Qual item é um periférico de entrada?",
                    listOf("Teclado", "Monitor", "Fonte", "SSD"),
                    "Teclado",
                    mapOf("Teclado" to R.drawable.ic_teclado, "Monitor" to R.drawable.ic_sistema, "Fonte" to R.drawable.ic_manutencao_basica, "SSD" to R.drawable.ic_arquivos),
                    "Periférico de entrada envia dados para o computador."
                )
            )
        ),
        "sistema" to module(
            "sistema",
            "Sistema Operacional",
            listOf(
                topic("Função do SO", "O sistema operacional gerencia programas, arquivos e recursos do dispositivo.", R.drawable.ic_sistema),
                topic("Área de trabalho", "A área de trabalho reúne atalhos, barra de tarefas e notificações.", R.drawable.ic_sistema),
                topic("Menu e configurações", "Use menu iniciar e configurações para abrir apps e ajustar o sistema.", R.drawable.ic_sistema)
            ),
            listOf(
                mc("sistema_mc1", "Qual item abre programas e configurações no Windows?", listOf("Lixeira", "Menu Iniciar", "Monitor", "Scanner"), "Menu Iniciar", "Ele fica na barra de tarefas."),
                dragActivity("sistema_drag", "Arraste para o local correto.", mapOf("Menu Iniciar" to "Barra de tarefas", "Ícone de arquivo" to "Área de trabalho", "Relógio" to "Barra de tarefas", "Atalho" to "Área de trabalho"), listOf("Área de trabalho", "Barra de tarefas"), "A barra de tarefas fica normalmente na parte inferior."),
                visual("sistema_visual", "Qual elemento representa a lixeira?", listOf("Lixeira", "Navegador", "Calculadora", "Teclado"), "Lixeira", mapOf("Lixeira" to R.drawable.ic_arquivos, "Navegador" to R.drawable.ic_internet, "Calculadora" to R.drawable.ic_teclado, "Teclado" to R.drawable.ic_teclado), "A lixeira guarda arquivos excluídos temporariamente.")
            )
        ),
        "arquivos" to module(
            "arquivos",
            "Arquivos e Pastas",
            listOf(
                topic("Pasta", "Pasta organiza arquivos como uma gaveta organiza documentos.", R.drawable.ic_arquivos),
                topic("Criar, renomear e mover", "Você pode criar pastas, mudar nomes e mover arquivos entre pastas.", R.drawable.ic_arquivos),
                topic("Tipos de arquivo", "Extensões como JPG, PDF e MP3 ajudam a identificar o tipo de conteúdo.", R.drawable.ic_arquivos)
            ),
            listOf(
                dragActivity("arquivos_drag", "Arraste cada extensão para seu tipo.", mapOf(".jpg" to "Imagem", ".mp3" to "Áudio", ".pdf" to "Documento", ".mp4" to "Vídeo"), listOf("Documento", "Imagem", "Áudio", "Vídeo"), "A extensão fica no final do nome do arquivo."),
                mc("arquivos_mc", "Qual ação envia um arquivo para a lixeira?", listOf("Renomear", "Excluir", "Copiar", "Abrir"), "Excluir", "Excluir move o item para a lixeira."),
                visual("arquivos_visual", "Qual ícone representa uma pasta?", listOf("Pasta", "Arquivo de música", "Documento", "Lixeira"), "Pasta", mapOf("Pasta" to R.drawable.ic_arquivos, "Arquivo de música" to R.drawable.ic_multimidia, "Documento" to R.drawable.ic_email, "Lixeira" to R.drawable.ic_manutencao_basica), "Pastas servem para agrupar arquivos.")
            )
        ),
        "teclado" to module(
            "teclado",
            "Teclado",
            listOf(
                topic("Layout", "Teclado possui letras, números e símbolos organizados em blocos.", R.drawable.ic_teclado),
                topic("Atalhos", "Atalhos como Ctrl+C e Ctrl+V aceleram tarefas comuns.", R.drawable.ic_teclado),
                topic("Ergonomia", "Postura e posição das mãos reduzem esforço e aumentam conforto.", R.drawable.ic_teclado)
            ),
            listOf(
                mc("teclado_mc1", "Qual atalho copia um texto?", listOf("Ctrl+X", "Ctrl+V", "Ctrl+C", "Ctrl+Z"), "Ctrl+C", "Copiar = C de Copy."),
                dragActivity("teclado_drag", "Arraste os atalhos para a função certa.", mapOf("Ctrl+V" to "Colar", "Ctrl+Z" to "Desfazer", "Ctrl+S" to "Salvar", "Ctrl+A" to "Selecionar tudo"), listOf("Colar", "Desfazer", "Salvar", "Selecionar tudo"), "Tente lembrar das iniciais em inglês."),
                visual("teclado_visual", "Qual tecla confirma uma ação na maioria dos formulários?", listOf("Tab", "Shift", "Enter", "Alt"), "Enter", mapOf("Tab" to R.drawable.ic_teclado, "Shift" to R.drawable.ic_teclado, "Enter" to R.drawable.ic_teclado, "Alt" to R.drawable.ic_teclado), "Enter geralmente envia ou confirma.")
            )
        ),
        "internet" to module(
            "internet",
            "Internet",
            listOf(
                topic("Navegador", "Navegador é o programa usado para acessar sites.", R.drawable.ic_internet),
                topic("URL e busca", "URL é o endereço direto de um site; busca é usada para pesquisar.", R.drawable.ic_internet),
                topic("Segurança", "Sites com HTTPS e cadeado são mais confiáveis.", R.drawable.ic_seguranca)
            ),
            listOf(
                mc("internet_mc", "Onde você digita o endereço do site?", listOf("Barra de URL", "Caixa de entrada", "Menu Iniciar", "Calculadora"), "Barra de URL", "A barra de URL fica no topo do navegador."),
                dragActivity("internet_drag", "Arraste para a ação correta.", mapOf("www.exemplo.com" to "Digitar URL", "Pesquisar receitas" to "Usar busca", "https://" to "Site seguro", "Salvar favorito" to "Acesso rápido"), listOf("Digitar URL", "Usar busca", "Site seguro", "Acesso rápido"), "URL é endereço direto; busca é pesquisa."),
                visual("internet_visual", "Qual opção indica conexão segura?", listOf("HTTP sem cadeado", "HTTPS com cadeado", "Página sem endereço", "Pop-up de erro"), "HTTPS com cadeado", mapOf("HTTP sem cadeado" to R.drawable.ic_internet, "HTTPS com cadeado" to R.drawable.ic_seguranca, "Página sem endereço" to R.drawable.ic_sistema, "Pop-up de erro" to R.drawable.ic_manutencao_basica), "O cadeado mostra camada extra de segurança.")
            )
        ),
        "seguranca" to module(
            "seguranca",
            "Segurança Digital",
            listOf(
                topic("Senhas fortes", "Use letras maiúsculas e minúsculas, números e símbolos.", R.drawable.ic_seguranca),
                topic("Phishing", "Desconfie de mensagens urgentes pedindo senha e dados pessoais.", R.drawable.ic_email),
                topic("2FA e backup", "Autenticação em dois fatores e backup protegem sua conta e arquivos.", R.drawable.ic_armazenamento_nuvem)
            ),
            listOf(
                mc("seguranca_mc", "Qual senha é mais forte?", listOf("123456", "senha", "MeuNome2020", "T3c#2026!"), "T3c#2026!", "Misturar caracteres aumenta segurança."),
                dragActivity("seguranca_drag", "Associe a prática com a finalidade.", mapOf("2FA" to "Camada extra", "Backup" to "Recuperar arquivos", "Atualizar sistema" to "Corrigir falhas", "Não clicar em links suspeitos" to "Evitar golpes"), listOf("Camada extra", "Recuperar arquivos", "Corrigir falhas", "Evitar golpes"), "Cada prática resolve um tipo de risco."),
                visual("seguranca_visual", "Qual sinal é comum em phishing?", listOf("Erro de português e urgência", "Domínio confiável", "Contato conhecido", "Senha gerada por app"), "Erro de português e urgência", mapOf("Erro de português e urgência" to R.drawable.ic_email, "Domínio confiável" to R.drawable.ic_internet, "Contato conhecido" to R.drawable.ic_email, "Senha gerada por app" to R.drawable.ic_seguranca), "Golpistas usam medo e pressa para enganar.")
            )
        ),
        "dispositivos_moveis" to module(
            "dispositivos_moveis",
            "Dispositivos Móveis",
            listOf(
                topic("Tela inicial", "A tela inicial mostra apps, widgets e atalhos.", R.drawable.ic_dispositivos_moveis),
                topic("Apps", "Apps são programas instalados em smartphone e tablet.", R.drawable.ic_dispositivos_moveis),
                topic("Gestos", "Toques, arrastar e pinça ajudam na navegação.", R.drawable.ic_dispositivos_moveis)
            ),
            listOf(
                mc("moveis_mc", "Onde você baixa aplicativos no Android?", listOf("Play Store", "Word", "Calculadora", "Bluetooth"), "Play Store", "A Play Store instala apps no Android."),
                dragActivity("moveis_drag", "Arraste para a categoria correta.", mapOf("WhatsApp" to "Aplicativo", "Área de notificações" to "Sistema", "Instagram" to "Aplicativo", "Brilho" to "Configuração rápida"), listOf("Aplicativo", "Sistema", "Configuração rápida"), "Apps são instalados; ajustes ficam nas configurações."),
                visual("moveis_visual", "Qual gesto aumenta o zoom em uma foto?", listOf("Pinça abrindo", "Toque duplo no botão", "Pressionar volume", "Deslizar para baixo"), "Pinça abrindo", mapOf("Pinça abrindo" to R.drawable.ic_dispositivos_moveis, "Toque duplo no botão" to R.drawable.ic_dispositivos_moveis, "Pressionar volume" to R.drawable.ic_dispositivos_moveis, "Deslizar para baixo" to R.drawable.ic_dispositivos_moveis), "Abra dois dedos para ampliar.")
            )
        ),
        "armazenamento_nuvem" to module(
            "armazenamento_nuvem",
            "Armazenamento em Nuvem",
            listOf(
                topic("Nuvem", "Nuvem permite acessar arquivos de qualquer lugar.", R.drawable.ic_armazenamento_nuvem),
                topic("Upload e sincronização", "Arquivos enviados ficam sincronizados entre dispositivos.", R.drawable.ic_armazenamento_nuvem),
                topic("Compartilhamento", "Você pode compartilhar por link com controle de acesso.", R.drawable.ic_armazenamento_nuvem)
            ),
            listOf(
                mc("nuvem_mc", "Onde salvar para acessar em vários dispositivos?", listOf("Nuvem", "Área de trabalho local", "Lixeira", "Pendrive desconectado"), "Nuvem", "A nuvem sincroniza entre dispositivos."),
                dragActivity("nuvem_drag", "Associe o termo à função.", mapOf("Upload" to "Enviar arquivo", "Download" to "Baixar arquivo", "Compartilhar link" to "Acesso para outra pessoa", "Backup" to "Recuperação"), listOf("Enviar arquivo", "Baixar arquivo", "Acesso para outra pessoa", "Recuperação"), "Upload sobe; download desce."),
                visual("nuvem_visual", "Qual serviço é de nuvem?", listOf("Google Drive", "Calculadora", "Bloco de notas", "Scanner físico"), "Google Drive", mapOf("Google Drive" to R.drawable.ic_armazenamento_nuvem, "Calculadora" to R.drawable.ic_teclado, "Bloco de notas" to R.drawable.ic_arquivos, "Scanner físico" to R.drawable.ic_impressao_scanner), "Drive e OneDrive são exemplos de nuvem.")
            )
        ),
        "impressao_scanner" to module(
            "impressao_scanner",
            "Impressão e Scanners",
            listOf(
                topic("Tipos de impressora", "Jato de tinta e laser possuem usos e custos diferentes.", R.drawable.ic_impressao_scanner),
                topic("Configuração", "Antes de imprimir, selecione páginas, qualidade e cor.", R.drawable.ic_impressao_scanner),
                topic("Scanner", "Digitalizar transforma papel em arquivo digital.", R.drawable.ic_impressao_scanner)
            ),
            listOf(
                mc("print_mc", "Qual ação digitaliza um documento de papel?", listOf("Escanear", "Formatar", "Deletar", "Compactar"), "Escanear", "Scan converte papel para arquivo digital."),
                dragActivity("print_drag", "Associe problema e solução.", mapOf("Sem tinta" to "Trocar cartucho", "Papel preso" to "Remover papel com cuidado", "Não liga" to "Verificar energia", "Wi-Fi instável" to "Reconectar rede"), listOf("Trocar cartucho", "Remover papel com cuidado", "Verificar energia", "Reconectar rede"), "A solução depende da causa do problema."),
                visual("print_visual", "Qual item representa um scanner?", listOf("Scanner", "Mouse", "Roteador", "Monitor"), "Scanner", mapOf("Scanner" to R.drawable.ic_impressao_scanner, "Mouse" to R.drawable.ic_hardware, "Roteador" to R.drawable.ic_internet, "Monitor" to R.drawable.ic_sistema), "Scanner captura imagem de papel.")
            )
        ),
        "multimidia" to module(
            "multimidia",
            "Multimídia",
            listOf(
                topic("Formatos", "JPG/PNG para imagem, MP3 para áudio e MP4 para vídeo.", R.drawable.ic_multimidia),
                topic("Edição simples", "Cortar, girar e redimensionar ajudam no uso diário.", R.drawable.ic_multimidia),
                topic("Players", "Players permitem reproduzir músicas e vídeos.", R.drawable.ic_multimidia)
            ),
            listOf(
                mc("multimidia_mc", "Qual formato é normalmente de música?", listOf("MP3", "JPG", "PDF", "DOC"), "MP3", "MP3 é áudio."),
                dragActivity("multimidia_drag", "Associe extensão e tipo.", mapOf(".png" to "Imagem", ".mp4" to "Vídeo", ".mp3" to "Áudio", ".pdf" to "Documento"), listOf("Imagem", "Vídeo", "Áudio", "Documento"), "Observe as extensões."),
                visual("multimidia_visual", "Qual app é mais usado para reproduzir vídeo?", listOf("VLC", "Calculadora", "Agenda", "Relógio"), "VLC", mapOf("VLC" to R.drawable.ic_multimidia, "Calculadora" to R.drawable.ic_teclado, "Agenda" to R.drawable.ic_email, "Relógio" to R.drawable.ic_sistema), "VLC é um player multimídia popular.")
            )
        ),
        "manutencao_basica" to module(
            "manutencao_basica",
            "Manutenção Básica",
            listOf(
                topic("Limpeza", "Remova arquivos temporários e esvazie a lixeira regularmente.", R.drawable.ic_manutencao_basica),
                topic("Atualizações", "Atualizar sistema e apps reduz falhas e riscos.", R.drawable.ic_manutencao_basica),
                topic("Prevenção", "Backup e antivírus ajudam a manter o computador saudável.", R.drawable.ic_manutencao_basica)
            ),
            listOf(
                mc("manutencao_mc", "Se o PC está lento, qual ação é recomendada?", listOf("Ignorar", "Limpar temporários", "Desligar monitor", "Trocar teclado"), "Limpar temporários", "Arquivos temporários podem consumir espaço."),
                dragActivity("manutencao_drag", "Associe tarefa ao objetivo.", mapOf("Atualizar sistema" to "Segurança", "Backup" to "Recuperação", "Scan antivírus" to "Remover ameaças", "Limpeza de disco" to "Liberar espaço"), listOf("Segurança", "Recuperação", "Remover ameaças", "Liberar espaço"), "Cada manutenção tem um objetivo."),
                visual("manutencao_visual", "Qual item ajuda a detectar malware?", listOf("Antivírus", "Editor de texto", "Calculadora", "Galeria"), "Antivírus", mapOf("Antivírus" to R.drawable.ic_seguranca, "Editor de texto" to R.drawable.ic_arquivos, "Calculadora" to R.drawable.ic_teclado, "Galeria" to R.drawable.ic_multimidia), "Antivírus identifica e remove ameaças.")
            )
        ),
        "email" to module(
            "email",
            "E-mail",
            listOf(
                topic("Estrutura", "Um e-mail tem campos Para, Assunto e Corpo da mensagem.", R.drawable.ic_email),
                topic("Anexos", "Você pode enviar arquivos como fotos e documentos em anexo.", R.drawable.ic_email),
                topic("Segurança", "Evite phishing e nunca compartilhe senha por e-mail.", R.drawable.ic_seguranca)
            ),
            listOf(
                mc("email_mc", "Qual campo recebe o destinatário?", listOf("Assunto", "Para", "Anexo", "Spam"), "Para", "O campo Para define quem receberá."),
                dragActivity("email_drag", "Associe ação ao recurso.", mapOf("Responder" to "Enviar retorno", "Encaminhar" to "Repassar mensagem", "Anexar arquivo" to "Enviar documento", "Marcar spam" to "Bloquear golpe"), listOf("Enviar retorno", "Repassar mensagem", "Enviar documento", "Bloquear golpe"), "Cada botão tem uma função específica."),
                visual("email_visual", "Qual mensagem parece phishing?", listOf("Urgente: confirme senha agora", "Reunião amanhã às 10h", "Comprovante de consulta", "Aviso da escola"), "Urgente: confirme senha agora", mapOf("Urgente: confirme senha agora" to R.drawable.ic_seguranca, "Reunião amanhã às 10h" to R.drawable.ic_email, "Comprovante de consulta" to R.drawable.ic_email, "Aviso da escola" to R.drawable.ic_email), "Golpes pedem senha e usam urgência.")
            )
        )
    )

    private fun module(
        moduleId: String,
        title: String,
        topics: List<TopicItem>,
        activities: List<PracticeActivityItem>
    ) = ModuleContent(moduleId, title, topics, activities)

    private fun topic(title: String, description: String, iconRes: Int) =
        TopicItem(title = title, description = description, iconResId = iconRes)

    private fun mc(
        id: String,
        question: String,
        options: List<String>,
        correct: String,
        hint: String
    ) = PracticeActivityItem(
        id = id,
        type = ActivityType.MULTIPLE_CHOICE,
        question = question,
        options = options,
        correctOption = correct,
        hint = hint
    )

    private fun visual(
        id: String,
        question: String,
        options: List<String>,
        correct: String,
        icons: Map<String, Int>,
        hint: String
    ) = PracticeActivityItem(
        id = id,
        type = ActivityType.VISUAL_IDENTIFICATION,
        question = question,
        options = options,
        correctOption = correct,
        hint = hint,
        visualOptionIcons = icons
    )

    private fun dragActivity(
        id: String,
        question: String,
        mapping: Map<String, String>,
        targets: List<String>,
        hint: String
    ) = PracticeActivityItem(
        id = id,
        type = ActivityType.DRAG_AND_DROP,
        question = question,
        options = mapping.keys.toList(),
        hint = hint,
        dragTargets = targets,
        dragCorrectMapping = mapping
    )
}
