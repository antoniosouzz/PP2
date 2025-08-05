package jobs;

// Importa a classe Imovel do pacote models.
import models.Imovel;
// Importa a classe TipoImovel do pacote models.
import models.TipoImovel;
// Importa a classe Job do Play Framework.
import play.jobs.Job;
// Importa a anotação OnApplicationStart, que faz este Job ser executado no início da aplicação.
import play.jobs.OnApplicationStart;

// A anotação @OnApplicationStart faz com que a classe Inicializador seja executada
// automaticamente quando a aplicação é iniciada.
@OnApplicationStart
public class Inicializador extends Job {
	
	/**
	 * O método doJob() é o que contém a lógica a ser executada pelo Job.
	 * Ele é chamado automaticamente pelo Play Framework.
	 * @throws Exception Lança uma exceção se algo der errado durante a execução.
	 */
	@Override
	public void doJob() throws Exception {
		// Inicia um bloco de verificação.
		// O método count() retorna o número de registros na tabela TipoImovel.
		// Esta verificação garante que os tipos de imóveis sejam criados apenas uma vez.
		if (TipoImovel.count() == 0) {
			// Cria um novo objeto TipoImovel com a descrição "CASA".
			TipoImovel casa = new TipoImovel("CASA");
			// Cria um novo objeto TipoImovel com a descrição "APARTAMENTO".
			TipoImovel apartamento = new TipoImovel("APARTAMENTO");
			// Cria um novo objeto TipoImovel com a descrição "CHALÉ".
			TipoImovel chale = new TipoImovel("CHALÉ");
			// Salva o objeto 'casa' no banco de dados, criando um novo registro.
			casa.save();
			// Salva o objeto 'apartamento' no banco de dados.
			apartamento.save();
			// Salva o objeto 'chale' no banco de dados.
			chale.save();
		}
		// O bloco de código de criação de imóveis foi removido para que
		// a aplicação inicie sem dados de teste, como você solicitou.
	}
}