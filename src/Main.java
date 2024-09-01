/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import buscaweb.CapturaRecursosWeb;

import java.util.ArrayList;

/**
 * @author Santiago
 */
public class Main {

	// busca char em vetor e retorna indice
	public static int get_char_ref(char[] vet, char ref) {
		for (int i = 0; i < vet.length; i++) {
			if (vet[i] == ref) {
				return i;
			}
		}
		return -1;
	}

	// busca string em vetor e retorna indice
	public static int get_string_ref(String[] vet, String ref) {
		for (int i = 0; i < vet.length; i++) {
			if (vet[i].equals(ref)) {
				return i;
			}
		}
		return -1;
	}


	//retorna o próximo estado, dado o estado atual e o símbolo lido
	public static int proximo_estado(char[] alfabeto, int[][] matriz, int estado_atual, char simbolo) {
		int simbol_indice = get_char_ref(alfabeto, simbolo);
		if (simbol_indice != -1) {
			return matriz[estado_atual][simbol_indice];
		} else {
			return -1;
		}
	}

	/*
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		//instancia e usa objeto que captura código-fonte de páginas Web
		var crw = new CapturaRecursosWeb();
		crw.getListaRecursos().add("https://www.univali.br/");
		var listaCodigos = crw.carregarRecursos();

		String codigoHTML = listaCodigos.get(0);

		//mapa do alfabeto
		char[] alfabeto = new char[11];
		alfabeto[0]  = '0';
		alfabeto[1]  = '1';
		alfabeto[2]  = '2';
		alfabeto[3]  = '3';
		alfabeto[4]  = '4';
		alfabeto[5]  = '5';
		alfabeto[6]  = '6';
		alfabeto[7]  = '7';
		alfabeto[8]  = '8';
		alfabeto[9]  = '9';
		alfabeto[10] = '-';


		//mapa de estados
		var estados = new String[32];

		for (int i = 0; i < 32; i++)
		{
			estados[i] = String.format("q%d", i);
		}

		String estado_inicial = "q0";

		//estados finais
		String[] estados_finais = new String[1];
		estados_finais[0] = "q25";

		//tabela de transição de AFD para reconhecimento números de dois dígitos
		int[][] matriz = new int[32][11];

		class Context
		{
			final private char[]    alfabeto;
			final private String[]  estados;
			final private int[][]   matriz;
			public Context(char[] alfabetoRef, String[] estadosRef, int[][] matrizRef)
			{
				this.alfabeto = alfabetoRef;
				this.estados  = estadosRef;
				this.matriz   = matrizRef;
			}
			public void applyHifen(String entrada, String saida)
			{
				this.matriz[get_string_ref(this.estados, entrada)][get_char_ref(alfabeto, '-')] = get_string_ref(estados, saida);
			}
			public void apply7(String entrada, String saida)
			{
				this.matriz[get_string_ref(this.estados, entrada)][get_char_ref(alfabeto, '7')] = get_string_ref(estados, saida);
			}
			public void apply8(String entrada, String saida)
			{
				this.matriz[get_string_ref(this.estados, entrada)][get_char_ref(alfabeto, '8')] = get_string_ref(estados, saida);
			}
			public void apply9(String entrada, String saida)
			{
				this.matriz[get_string_ref(this.estados, entrada)][get_char_ref(alfabeto, '9')] = get_string_ref(estados, saida);
			}
			public void apply89(String entrada, String saida)
			{
				this.apply8(entrada, saida);
				this.apply9(entrada, saida);
			}
			public void applyNum(String entrada, String saida)
			{
				for (int i = 0; i <= 9; i++)
				{
					this.matriz[get_string_ref(this.estados, entrada)][get_char_ref(alfabeto, Character.forDigit(i, 10))] = get_string_ref(estados, saida);
				}
			}
			public void applyAlfabet(String entrada, String saida)
			{
				this.applyNum(entrada, saida);
				this.applyHifen(entrada, saida);
			}
			public void voidStates()
			{
				for (int j = 0; j < 32; j++) {
					for (int i = 0; i <= 9; i++) {
						this.matriz[get_string_ref(this.estados, String.format("q%d", j))][get_char_ref(alfabeto, Character.forDigit(i, 10))] = -1;
					}
					this.matriz[get_string_ref(this.estados, String.format("q%d", j))][get_char_ref(alfabeto, '-')] = -1;
				}
			}
		}

		var ctx = new Context(alfabeto, estados, matriz);

		ctx.voidStates();

		ctx.apply9    ("q0", "q1"); // q0('9')  → q1
		ctx.apply7    ("q1", "q2"); // q1('7')  → q2
		ctx.apply89   ("q2", "q3"); // q2('89') → q3

		ctx.applyHifen("q3", "q4"); // ..
		ctx.applyNum  ("q3", "q5");
		ctx.applyNum  ("q4", "q5");

		ctx.applyHifen("q5", "q17");
		ctx.applyNum  ("q5", "q6");
		ctx.applyHifen("q6", "q26");
		ctx.applyNum  ("q6", "q7");
		ctx.applyHifen("q7", "q27");
		ctx.applyNum  ("q7", "q8");
		ctx.applyHifen("q8", "q28");
		ctx.applyNum  ("q8", "q9");
		ctx.applyHifen("q9", "q29");
		ctx.applyNum  ("q9", "q14");
		ctx.applyHifen("q10", "q31");
		ctx.applyNum  ("q10", "q11");
		ctx.applyHifen("q11", "q18");
		ctx.applyNum  ("q11", "q12");
		ctx.applyHifen("q12", "q19");
		ctx.applyNum  ("q12", "q13");
		ctx.applyHifen("q13", "q20");
		ctx.applyNum  ("q13", "q14");
		ctx.applyHifen("q14", "q21");
		ctx.applyNum  ("q14", "q15");
		ctx.applyHifen("q15", "q22");
		ctx.applyNum  ("q15", "q16");
		ctx.applyHifen("q16", "q23");
		ctx.applyNum  ("q16", "q30");

		ctx.applyNum  ("q17", "q10");
		ctx.applyNum  ("q18", "q19");
		ctx.applyNum  ("q19", "q20");
		ctx.applyNum  ("q20", "q21");
		ctx.applyNum  ("q21", "q22");
		ctx.applyNum  ("q22", "q23");
		ctx.applyNum  ("q23", "q30");
		ctx.applyNum  ("q24", "q25");

		/* q25 is final */

		ctx.applyNum  ("q26", "q11");
		ctx.applyNum  ("q27", "q12");
		ctx.applyNum  ("q28", "q13");
		ctx.applyNum  ("q29", "q14");

		ctx.applyHifen("q30", "q25");
		ctx.applyNum  ("q30", "q25");

		ctx.applyNum  ("q31", "q11");

		int estado = get_string_ref(estados, estado_inicial);
		int estado_anterior = -1;
		ArrayList<String> palavras_reconhecidas = new ArrayList<>();

		String palavra = "";

		//varre o código-fonte de um código
		for (int i = 0; i < codigoHTML.length(); i++) {

			estado_anterior = estado;
			estado = proximo_estado(alfabeto, matriz, estado, codigoHTML.charAt(i));
			//se o não há transição
			if (estado == -1) {
				//pega estado inicial
				estado = get_string_ref(estados, estado_inicial);
				// se o estado anterior foi um estado final
				if (get_string_ref(estados_finais, estados[estado_anterior]) != -1) {
					//se a palavra não é vazia adiciona palavra reconhecida
					if (!palavra.isEmpty()) {
						palavras_reconhecidas.add(palavra);
					}
					// se ao analisar este caracter não houve transição
					// teste-o novamente, considerando que o estado seja inicial
					i--;
				}
				//zera palavra
				palavra = "";

			} else {
				//se houver transição válida, adiciona caracter a palavra
				palavra += codigoHTML.charAt(i);
			}
		}

		//foreach no Java para exibir todas as palavras reconhecidas
		for (String p : palavras_reconhecidas) {
			System.out.println(p);
		}
	}
}