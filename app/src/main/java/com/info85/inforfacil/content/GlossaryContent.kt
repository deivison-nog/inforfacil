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
        t("cpu", "CPU", "Cérebro do computador.", "Processa instruções e coordena tarefas.", android.R.drawable.ic_menu_manage),
        t("monitor", "Monitor", "Tela de exibição.", "Mostra textos, imagens e vídeos.", R.drawable.ic_glossary_monitor),
        t("teclado", "Teclado", "Dispositivo de digitação.", "Permite inserir letras, números e comandos.", R.drawable.ic_teclado),
        t("mouse", "Mouse", "Dispositivo apontador.", "Usado para clicar, arrastar e navegar.", R.drawable.ic_glossary_mouse),
        t("placa_mae", "Placa-mãe", "Conecta componentes.", "Liga processador, memória e periféricos.", R.drawable.ic_glossary_motherboard),
        t("ssd", "SSD", "Armazenamento rápido.", "Guarda arquivos com maior velocidade que HD.", android.R.drawable.stat_notify_sdcard),
        t("hd", "HD", "Disco rígido.", "Armazena dados de forma magnética.", R.drawable.ic_glossary_hdd),
        t("fonte", "Fonte de alimentação", "Fornece energia.", "Converte energia para os componentes do PC.", R.drawable.ic_glossary_power_supply),
        t("desktop", "Área de Trabalho", "Tela principal.", "Local de atalhos e arquivos frequentes.", android.R.drawable.ic_menu_view),
        t("taskbar", "Barra de Tarefas", "Faixa inferior do Windows.", "Mostra programas abertos, menu e relógio.", android.R.drawable.ic_menu_sort_by_size),
        t("start_menu", "Menu Iniciar", "Menu de apps e opções.", "Permite abrir programas e configurações.", android.R.drawable.ic_menu_more),
        t("explorer", "Explorador de Arquivos", "Gerenciador de pastas.", "Organiza documentos e diretórios.", R.drawable.ic_arquivos),
        t("lixeira", "Lixeira", "Armazena itens excluídos.", "Permite restaurar ou apagar definitivamente.", android.R.drawable.ic_menu_delete),
        t("pasta", "Pasta", "Agrupa arquivos.", "Ajuda na organização por assunto ou tipo.", R.drawable.ic_arquivos),
        t("arquivo", "Arquivo", "Unidade de informação.", "Pode conter texto, imagem, áudio ou vídeo.", R.drawable.ic_glossary_file),
        t("extensao", "Extensão", "Sufixo do arquivo.", "Indica o formato, como .jpg ou .pdf.", android.R.drawable.ic_menu_info_details),
        t("atalho", "Atalho", "Combinação de teclas.", "Executa ações rapidamente, como Ctrl+C.", android.R.drawable.ic_menu_directions),
        t("shift", "Shift", "Tecla especial.", "Faz maiúsculas e acessa símbolos superiores.", R.drawable.ic_teclado),
        t("ctrl", "Ctrl", "Tecla de comando.", "Usada em atalhos com outras teclas.", R.drawable.ic_teclado),
        t("alt", "Alt", "Tecla alternativa.", "Ativa comandos adicionais em atalhos.", R.drawable.ic_teclado),
        t("enter", "Enter", "Tecla de confirmação.", "Confirma formulários e quebra linha.", R.drawable.ic_teclado),
        t("internet", "Internet", "Rede mundial.", "Conecta dispositivos e serviços digitais.", R.drawable.ic_internet),
        t("navegador", "Navegador", "App para acessar sites.", "Chrome, Edge e Firefox são exemplos.", android.R.drawable.ic_menu_search),
        t("url", "URL", "Endereço de site.", "Exemplo: https://www.site.com.", android.R.drawable.ic_menu_share),
        t("aba", "Aba", "Guia do navegador.", "Permite abrir várias páginas ao mesmo tempo.", android.R.drawable.ic_menu_more),
        t("favoritos", "Favoritos", "Sites salvos.", "Facilitam acesso rápido a páginas frequentes.", android.R.drawable.btn_star_big_on),
        t("historico", "Histórico", "Lista de sites visitados.", "Ajuda a reencontrar páginas acessadas.", android.R.drawable.ic_menu_recent_history),
        t("download", "Download", "Baixar arquivo.", "Move arquivo da internet para o dispositivo.", android.R.drawable.stat_sys_download_done),
        t("upload", "Upload", "Enviar arquivo.", "Move arquivo do dispositivo para internet.", android.R.drawable.stat_sys_upload_done),
        t("https", "HTTPS", "Conexão segura.", "Usa criptografia para proteger dados.", android.R.drawable.ic_lock_lock),
        t("senha", "Senha", "Código de acesso.", "Deve ser forte e exclusiva.", android.R.drawable.ic_lock_lock),
        t("2fa", "Autenticação 2 fatores", "Camada extra de segurança.", "Exige senha + código adicional.", android.R.drawable.ic_lock_idle_lock),
        t("phishing", "Phishing", "Golpe por mensagem falsa.", "Tenta roubar dados simulando empresa confiável.", android.R.drawable.stat_sys_warning),
        t("antivirus", "Antivírus", "Proteção contra malware.", "Detecta e remove ameaças digitais.", R.drawable.ic_seguranca),
        t("backup", "Backup", "Cópia de segurança.", "Permite recuperar arquivos perdidos.", android.R.drawable.ic_popup_sync),
        t("wifi", "Wi-Fi", "Rede sem fio.", "Conecta dispositivos à internet sem cabos.", R.drawable.ic_internet),
        t("nuvem", "Nuvem", "Armazenamento online.", "Arquivos disponíveis em vários dispositivos.", R.drawable.ic_armazenamento_nuvem),
        t("google_drive", "Google Drive", "Serviço de nuvem.", "Cria, salva e compartilha arquivos online.", R.drawable.ic_armazenamento_nuvem),
        t("onedrive", "OneDrive", "Nuvem da Microsoft.", "Integra com Windows e Office.", R.drawable.ic_armazenamento_nuvem),
        t("sincronizacao", "Sincronização", "Atualização automática.", "Mantém mesmos arquivos em dispositivos diferentes.", android.R.drawable.ic_popup_sync),
        t("scanner", "Scanner", "Digitaliza documentos.", "Converte papel para arquivo digital.", android.R.drawable.ic_menu_camera),
        t("impressora", "Impressora", "Imprime no papel.", "Pode ser jato de tinta ou laser.", R.drawable.ic_impressao_scanner),
        t("toner", "Toner", "Pó de impressora laser.", "Consumível usado em impressoras a laser.", R.drawable.ic_impressao_scanner),
        t("cartucho", "Cartucho", "Reservatório de tinta.", "Consumível de impressora jato de tinta.", R.drawable.ic_impressao_scanner),
        t("jpg", "JPG", "Formato de imagem.", "Muito usado em fotos.", android.R.drawable.ic_menu_gallery),
        t("png", "PNG", "Imagem com transparência.", "Formato de boa qualidade para gráficos.", android.R.drawable.ic_menu_crop),
        t("mp3", "MP3", "Formato de áudio.", "Usado para músicas e gravações.", android.R.drawable.ic_media_play),
        t("mp4", "MP4", "Formato de vídeo.", "Combina vídeo e áudio.", android.R.drawable.ic_media_ff),
        t("pdf", "PDF", "Documento portátil.", "Mantém layout em qualquer dispositivo.", android.R.drawable.ic_menu_save),
        t("email", "E-mail", "Correio eletrônico.", "Envia e recebe mensagens pela internet.", R.drawable.ic_email),
        t("anexo", "Anexo", "Arquivo em e-mail.", "Pode ser foto, PDF, planilha e outros.", R.drawable.ic_glossary_file),
        t("spam", "Spam", "Mensagem indesejada.", "Pode conter propaganda ou golpes.", android.R.drawable.stat_notify_error),
        t("reply", "Responder", "Enviar resposta ao remetente.", "Retorna mensagem para quem enviou.", android.R.drawable.ic_menu_revert),
        t("forward", "Encaminhar", "Repassar e-mail.", "Envia mensagem recebida para outra pessoa.", android.R.drawable.ic_menu_send)
    )

    private fun t(id: String, term: String, definition: String, details: String, iconResId: Int) =
        GlossaryTerm(id, term, definition, details, iconResId)
}
