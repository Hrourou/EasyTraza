package cat.copernic.easytraza.albaraproveidor.ui.viewmodel

import cat.copernic.easytraza.albaraproveidor.model.OcrLiniaRespostaDto
import cat.copernic.easytraza.albaraproveidor.model.OcrRespostaDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AlbaraFormulariEstat(
    val cifProveidor: String = "",
    val nomProveidor: String? = null,
    val dataRecepcio: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val fotoAlbaraUrl: String? = null,
    val liniesLote: List<OcrLiniaRespostaDto> = listOf(OcrLiniaRespostaDto())
)

fun AlbaraFormulariEstat.toDto() = OcrRespostaDto(
    cifProveidor = cifProveidor,
    nomProveidor = nomProveidor,
    dataRecepcio = dataRecepcio,
    fotoAlbaraUrl = fotoAlbaraUrl,
    liniesLote = liniesLote
)

fun OcrRespostaDto.toEstat() = AlbaraFormulariEstat(
    cifProveidor = cifProveidor,
    nomProveidor = nomProveidor,
    dataRecepcio = dataRecepcio,
    fotoAlbaraUrl = fotoAlbaraUrl,
    liniesLote = if (liniesLote.isEmpty()) listOf(OcrLiniaRespostaDto()) else liniesLote
)
