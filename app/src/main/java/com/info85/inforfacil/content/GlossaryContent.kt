package com.info85.inforfacil.content

import com.info85.inforfacil.R

data class GlossaryTerm(
    val id: String,
    val term: String,
    val definition: String,
    val details: String,
    val iconResId: Int
)

object GlossaryContent {
    fun terms(): List<GlossaryTerm> = listOf(
        t("cpu", "CPU", "Cérebro do computador.", "Processa instruções e coordena tarefas.", R.drawable.ic_hardware),
        t("monitor", "Monitor", "Tela de exibição.", "Mostra textos, imagens e vídeos.", R.drawable.ic_sistema),
        t("teclado", "Teclado", "Dispositivo de digitação.", "Permite inserir letras, números e comandos.", R.drawable.ic_teclado),
        t("mouse", "Mouse", "Dispositivo apontador.", "Usado para clicar, arrastar e navegar.", R.drawable.ic_hardware),
        t("placa_mae", "Placa-mãe", "Conecta componentes.", "Liga processador, memória e periféricos.", R.drawable.ic_hardware),
        t("ssd", "SSD", "Armazenamento rápido.", "Guarda arquivos com maior velocidade que HD.", R.drawable.ic_arquivos),
        t("hd", "HD", "Disco rígido.", "Armazena dados de forma magnética.", R.drawable.ic_arquivos),
        t("fonte", "Fonte de alimentação", "Fornece energia.", "Converte energia para os componentes do PC.", R.drawable.ic_manutencao_basica),
        t("desktop", "Área de Trabalho", "Tela principal.", "Local de atalhos e arquivos frequentes.", R.drawable.ic_sistema),
        t("taskbar", "Barra de Tarefas", "Faixa inferior do Windows.", "Mostra programas abertos, menu e relógio.", R.drawable.ic_sistema),
        t("start_menu", "Menu Iniciar", "Menu de apps e opções.", "Permite abrir programas e configurações.", R.drawable.ic_sistema),
        t("explorer", "Explorador de Arquivos", "Gerenciador de pastas.", "Organiza documentos e diretórios.", R.drawable.ic_arquivos),
        t("lixeira", "Lixeira", "Armazena itens excluídos.", "Permite restaurar ou apagar definitivamente.", R.drawable.ic_arquivos),
        t("pasta", "Pasta", "Agrupa arquivos.", "Ajuda na organização por assunto ou tipo.", R.drawable.ic_arquivos),
        t("arquivo", "Arquivo", "Unidade de informação.", "Pode conter texto, imagem, áudio ou vídeo.", R.drawable.ic_arquivos),
        t("extensao", "Extensão", "Sufixo do arquivo.", "Indica o formato, como .jpg ou .pdf.", R.drawable.ic_arquivos),
        t("atalho", "Atalho", "Combinação de teclas.", "Executa ações rapidamente, como Ctrl+C.", R.drawable.ic_teclado),
        t("shift", "Shift", "Tecla especial.", "Faz maiúsculas e acessa símbolos superiores.", R.drawable.ic_teclado),
        t("ctrl", "Ctrl", "Tecla de comando.", "Usada em atalhos com outras teclas.", R.drawable.ic_teclado),
        t("alt", "Alt", "Tecla alternativa.", "Ativa comandos adicionais em atalhos.", R.drawable.ic_teclado),
        t("enter", "Enter", "Tecla de confirmação.", "Confirma formulários e quebra linha.", R.drawable.ic_teclado),
        t("internet", "Internet", "Rede mundial.", "Conecta dispositivos e serviços digitais.", R.drawable.ic_internet),
        t("navegador", "Navegador", "App para acessar sites.", "Chrome, Edge e Firefox são exemplos.", R.drawable.ic_internet),
        t("url", "URL", "Endereço de site.", "Exemplo: https://www.site.com.", R.drawable.ic_internet),
        t("aba", "Aba", "Guia do navegador.", "Permite abrir várias páginas ao mesmo tempo.", R.drawable.ic_internet),
        t("favoritos", "Favoritos", "Sites salvos.", "Facilitam acesso rápido a páginas frequentes.", R.drawable.ic_internet),
        t("historico", "Histórico", "Lista de sites visitados.", "Ajuda a reencontrar páginas acessadas.", R.drawable.ic_internet),
        t("download", "Download", "Baixar arquivo.", "Move arquivo da internet para o dispositivo.", R.drawable.ic_internet),
        t("upload", "Upload", "Enviar arquivo.", "Move arquivo do dispositivo para internet.", R.drawable.ic_armazenamento_nuvem),
        t("https", "HTTPS", "Conexão segura.", "Usa criptografia para proteger dados.", R.drawable.ic_seguranca),
        t("senha", "Senha", "Código de acesso.", "Deve ser forte e exclusiva.", R.drawable.ic_seguranca),
        t("2fa", "Autenticação 2 fatores", "Camada extra de segurança.", "Exige senha + código adicional.", R.drawable.ic_seguranca),
        t("phishing", "Phishing", "Golpe por mensagem falsa.", "Tenta roubar dados simulando empresa confiável.", R.drawable.ic_seguranca),
        t("antivirus", "Antivírus", "Proteção contra malware.", "Detecta e remove ameaças digitais.", R.drawable.ic_seguranca),
        t("backup", "Backup", "Cópia de segurança.", "Permite recuperar arquivos perdidos.", R.drawable.ic_armazenamento_nuvem),
        t("wifi", "Wi-Fi", "Rede sem fio.", "Conecta dispositivos à internet sem cabos.", R.drawable.ic_internet),
        t("nuvem", "Nuvem", "Armazenamento online.", "Arquivos disponíveis em vários dispositivos.", R.drawable.ic_armazenamento_nuvem),
        t("google_drive", "Google Drive", "Serviço de nuvem.", "Cria, salva e compartilha arquivos online.", R.drawable.ic_armazenamento_nuvem),
        t("onedrive", "OneDrive", "Nuvem da Microsoft.", "Integra com Windows e Office.", R.drawable.ic_armazenamento_nuvem),
        t("sincronizacao", "Sincronização", "Atualização automática.", "Mantém mesmos arquivos em dispositivos diferentes.", R.drawable.ic_armazenamento_nuvem),
        t("scanner", "Scanner", "Digitaliza documentos.", "Converte papel para arquivo digital.", R.drawable.ic_impressao_scanner),
        t("impressora", "Impressora", "Imprime no papel.", "Pode ser jato de tinta ou laser.", R.drawable.ic_impressao_scanner),
        t("toner", "Toner", "Pó de impressora laser.", "Consumível usado em impressoras a laser.", R.drawable.ic_impressao_scanner),
        t("cartucho", "Cartucho", "Reservatório de tinta.", "Consumível de impressora jato de tinta.", R.drawable.ic_impressao_scanner),
        t("jpg", "JPG", "Formato de imagem.", "Muito usado em fotos.", R.drawable.ic_multimidia),
        t("png", "PNG", "Imagem com transparência.", "Formato de boa qualidade para gráficos.", R.drawable.ic_multimidia),
        t("mp3", "MP3", "Formato de áudio.", "Usado para músicas e gravações.", R.drawable.ic_multimidia),
        t("mp4", "MP4", "Formato de vídeo.", "Combina vídeo e áudio.", R.drawable.ic_multimidia),
        t("pdf", "PDF", "Documento portátil.", "Mantém layout em qualquer dispositivo.", R.drawable.ic_multimidia),
        t("email", "E-mail", "Correio eletrônico.", "Envia e recebe mensagens pela internet.", R.drawable.ic_email),
        t("anexo", "Anexo", "Arquivo em e-mail.", "Pode ser foto, PDF, planilha e outros.", R.drawable.ic_email),
        t("spam", "Spam", "Mensagem indesejada.", "Pode conter propaganda ou golpes.", R.drawable.ic_email),
        t("reply", "Responder", "Enviar resposta ao remetente.", "Retorna mensagem para quem enviou.", R.drawable.ic_email),
        t("forward", "Encaminhar", "Repassar e-mail.", "Envia mensagem recebida para outra pessoa.", R.drawable.ic_email)
    )

    private fun t(id: String, term: String, definition: String, details: String, iconResId: Int) =
        GlossaryTerm(id, term, definition, details, iconResId)
}
