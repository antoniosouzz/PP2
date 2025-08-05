package controllers;

// Importa a classe List para trabalhar com coleções de objetos.
import java.util.List;

// Importa a classe Status do pacote models, que define o status do tipo de imóvel.
import models.Status;
// Importa a classe TipoImovel do pacote models.
import models.TipoImovel;
// Importa a classe Controller do framework Play para criar um controlador web.
import play.mvc.Controller;

// Declara a classe TipoImoveis que herda de Controller, tornando-a um controlador web.
public class TipoImoveis extends Controller {

	/**
	 * Método estático para exibir o formulário de cadastro ou edição de um tipo de imóvel.
	 * O Play Framework mapeia este método para uma URL, por exemplo: /TipoImoveis/form.
	 */
	public static void form() {
		// O método render() envia o controle para a view (TipoImoveis/form.html)
		// e a exibe para o usuário.
		render();
	}
	
	/**
	 * Método estático para listar todos os tipos de imóveis cadastrados.
	 * Ele também permite a funcionalidade de busca por termo.
	 * @param termo Parâmetro opcional de busca. O Play o preenche com o valor do input "termo".
	 */
	public static void listar(String termo) {
	
		List<TipoImovel> tiposImoveis = null;
		// Verifica se a variável 'termo' é nula. Isso acontece quando a página é acessada sem uma busca.
		if (termo == null) {
			// Se for nulo, a aplicação busca todos os TipoImovel que não estejam com o status INATIVO.
			tiposImoveis = TipoImovel.find("status <> ?1", Status.INATIVO).fetch();
		} else {
			// Se houver um termo, busca por TipoImovel onde a 'descricao' contenha o termo,
			// ignorando maiúsculas e minúsculas (a função lower()). A busca também filtra por status ATIVO.
			tiposImoveis = TipoImovel.find("lower(descricao) like ?1 and status <> ?2",
										"%" + termo.toLowerCase() + "%",
										Status.INATIVO).fetch();
		}
		// Renderiza a view de listagem (TipoImoveis/listar.html), passando a lista de tipos de imóveis
		// e o termo de busca para que a página possa exibir os resultados.
		render(tiposImoveis, termo);
	}
	
	/**
	 * Método estático para exibir os detalhes de um tipo de imóvel específico.
	 * @param id O ID do tipo de imóvel, passado como parâmetro na URL.
	 */
	 public static void detalhar(Long id) {
		// Encontra um TipoImovel no banco de dados usando seu ID único.
		TipoImovel tipoImovel = TipoImovel.findById(id);
		// Renderiza a view de detalhamento (TipoImoveis/detalhar.html), passando o objeto TipoImovel.
		render(tipoImovel);
	}
	
	/**
	 * Método estático para preencher o formulário para a edição de um tipo de imóvel.
	 * É similar ao método form(), mas carrega os dados de um tipo de imóvel existente.
	 * @param id O ID do tipo de imóvel a ser editado.
	 */
	 public static void editar(Long id) {
		// Encontra o tipo de imóvel a ser editado pelo ID.
		TipoImovel tipoImovel = TipoImovel.findById(id);
		// Renderiza o template de formulário (TipoImoveis/form.html) com o objeto tipoImovel.
		renderTemplate("TipoImoveis/form.html", tipoImovel);
	}
	
	/**
	 * Método estático para salvar ou atualizar um tipo de imóvel.
	 * Este método é acionado quando o formulário é submetido.
	 * @param tipoImovel O objeto TipoImovel é preenchido automaticamente com os dados do formulário.
	 */
	public static void salvar(TipoImovel tipoImovel) {
		// Salva ou atualiza o objeto TipoImovel no banco de dados.
		tipoImovel.save();
		// Redireciona para a página de detalhes do tipo de imóvel salvo.
		detalhar(tipoImovel.id);
	}
	
	/**
	 * Método estático para remover um tipo de imóvel de forma lógica.
	 * @param id O ID do tipo de imóvel a ser removido.
	 */
	public static void remover(Long id) {
		// Encontra o tipo de imóvel no banco de dados pelo ID.
		TipoImovel tipoImovel = TipoImovel.findById(id);
		// Verifica se o tipo de imóvel foi encontrado antes de tentar modificá-lo.
		if (tipoImovel != null) {
			// Altera o status do tipo de imóvel para INATIVO.
			tipoImovel.status = Status.INATIVO;
			// Salva a alteração no banco de dados.
			tipoImovel.save();
		}
		// Redireciona para a página de listagem de tipos de imóveis após a remoção.
		// O parâmetro 'null' garante que a listagem não aplique nenhum filtro.
		listar(null);
	}
}