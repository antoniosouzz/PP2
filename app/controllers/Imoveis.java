package controllers;

// Importa a classe List para trabalhar com coleções de objetos.
import java.util.List;

// Importa a classe Imovel do pacote models.
import models.Imovel;
// Importa a classe Status do pacote models, que define o status do tipo de imóvel.
import models.Status;
// Importa a classe TipoImovel do pacote models.
import models.TipoImovel;
// Importa a classe Controller do framework Play para criar um controlador.
import play.mvc.Controller;

// Declara a classe Imoveis que herda de Controller, tornando-a um controlador web.
public class Imoveis extends Controller {

	/**
	 * Método estático para exibir o formulário de cadastro de um novo imóvel ou edição de um existente.
	 * Ele é acessado pela URL /Imoveis/form e é responsável por preparar a view.
	 */
	public static void form() {
		// Busca todos os objetos TipoImovel que não estejam com o status INATIVO.
		// O método .fetch() recupera todos os resultados da busca.
		List<TipoImovel> tiposImoveis = TipoImovel.find("status <> ?1", Status.INATIVO).fetch();
		// O método render() envia a lista de tipos de imóveis para a view (Imoveis/form.html).
		// A view usará essa lista para popular a caixa de seleção (select) do formulário.
		render(tiposImoveis);
	}

	/**
	 * Método estático para listar todos os imóveis cadastrados.
	 * Ele também permite a funcionalidade de busca por termo.
	 * @param termo Parâmetro opcional de busca. O Play o preenche automaticamente com o valor do campo "termo".
	 */
	public static void listar(String termo) {
		List<Imovel> imoveis = null;
		// Verifica se a variável 'termo' é nula. Isso acontece quando a página é acessada sem uma busca.
		if (termo == null) {
			// Se não houver termo de busca, a aplicação busca e lista todos os imóveis existentes.
			imoveis = Imovel.findAll();
		} else {
			// Se houver um termo, o Play busca por imóveis onde a coluna 'bairro' ou 'codigoAnuncio'
			// contenham o texto do termo, ignorando maiúsculas e minúsculas (a função lower()).
			imoveis = Imovel
					.find("lower(bairro) like ?1 or lower(codigoAnuncio) like ?1", "%" + termo.toLowerCase() + "%")
					.fetch();
		}
		// Renderiza a view de listagem (Imoveis/listar.html), passando a lista de imóveis
		// e o termo de busca para que a página possa exibir os resultados filtrados.
		render(imoveis, termo);
	}

	/**
	 * Método estático para exibir os detalhes de um imóvel específico.
	 * @param id O ID do imóvel, passado como parâmetro na URL.
	 */
	public static void detalhar(Long id) {
		// Usa o método findById para buscar um imóvel no banco de dados usando seu ID único.
		Imovel imovel = Imovel.findById(id);
		// Renderiza a view de detalhamento (Imoveis/detalhar.html), passando o objeto Imovel encontrado.
		render(imovel);
	}

	/**
	 * Método estático para preencher o formulário para a edição de um imóvel.
	 * É similar ao método form(), mas carrega os dados de um imóvel existente.
	 * @param id O ID do imóvel a ser editado.
	 */
	public static void editar(Long id) {
		// Encontra o imóvel a ser editado pelo ID.
		Imovel imovel = Imovel.findById(id);
		// Busca a lista de tipos de imóveis ativos para popular a caixa de seleção do formulário.
		List<TipoImovel> tiposImoveis = TipoImovel.find("status <> ?1", models.Status.INATIVO).fetch();
		// Renderiza o template de formulário (Imoveis/form.html) com o imóvel a ser editado e a lista de tipos.
		renderTemplate("Imoveis/form.html", imovel, tiposImoveis);
	}

	/**
	 * Método estático para salvar ou atualizar um imóvel.
	 * Este método é acionado quando o formulário é submetido. Ele inclui uma validação para garantir
	 * que o código do anúncio seja único.
	 * @param imovel O objeto Imovel é preenchido automaticamente com os dados do formulário pelo Play.
	 */
	public static void salvar(Imovel imovel) {
		// 1. Inicia a verificação de unicidade do código do anúncio.
		// Busca um imóvel que tenha o mesmo código de anúncio digitado,
		// mas que não seja o mesmo imóvel que está sendo editado (verificação crucial para edições).
		Imovel existingImovel = Imovel.find("codigoAnuncio = ?1 and id <> ?2", imovel.codigoAnuncio, imovel.id).first();

		// 2. Se a busca encontrar um imóvel com código duplicado (ou seja, existingImovel não é nulo)...
		if (existingImovel != null) {
			// ...o sistema interrompe o processo de salvamento.
			// Define uma mensagem de erro que será exibida na próxima página.
			flash.error("Já existe um imóvel com este código de anúncio!");
			// Mantém os dados enviados no formulário na memória temporária (flash) para que
			// os campos do formulário não sejam apagados quando a página for recarregada.
			params.flash();
			if (imovel.id == null) {
				// Se for um novo cadastro, redireciona para o formulário de cadastro.
				form();
			} else {
				// Se for uma edição, redireciona para o formulário de edição do imóvel, mantendo o ID.
				editar(imovel.id);
			}
		}

		// 3. Este trecho de código SÓ será executado se NENHUMA duplicidade for encontrada.
		imovel.save(); // Salva ou atualiza o objeto Imovel no banco de dados.
		detalhar(imovel.id); // Redireciona para a página de detalhes do imóvel salvo com sucesso.
	}

	/**
	 * Método estático para remover um imóvel permanentemente do banco de dados.
	 * @param id O ID do imóvel a ser removido, passado na URL.
	 */
	public static void remover(Long id) {
		// Encontra o imóvel no banco de dados pelo ID.
		Imovel imovel = Imovel.findById(id);
		// Verifica se o imóvel foi encontrado antes de tentar removê-lo, para evitar erros.
		if (imovel != null) {
			imovel.delete(); // Remove o imóvel do banco de dados.
		}
		// Redireciona para a página de listagem de imóveis após a remoção.
		// O parâmetro 'null' garante que a listagem não aplique nenhum filtro de busca.
		listar(null);
	}
}