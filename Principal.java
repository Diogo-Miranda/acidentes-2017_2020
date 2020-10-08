import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Diogo Araujo Miranda
 */
public class Principal {

    public static char replaceVirgula(char in) {
        return ( (in == ',') ? ';' : in);
    }

    public static String tratarVirgula(String in) {
        String out = "";

        for(int i = 0; i < in.length(); i++) {
            out += replaceVirgula(in.charAt(i));
        }

        return out;
    }
    
    public static void ler(List<Acidente> list, String ano) {

        File f = new File("bases/"+ ano + ".csv");

        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
    
            // Ignorar a leitura da primeira linha
            String s = br.readLine();
            Acidente acidente;
            String[] dados;

            while ( (s = br.readLine()) != null ) {
                s =  tratarVirgula(s);
                acidente = new Acidente();
                dados = s.split(";");

                acidente.id = Long.parseLong(dados[0]);
                acidente.dataInversa = dados[1];
                acidente.diaSemana = dados[2];
                acidente.horario = dados[3];
                acidente.uf = dados[4];
                acidente.municipio = dados[5];
                acidente.causaAciente = dados[6];
                acidente.faseDia = dados[7];
                acidente.condicaoMeteologica = dados[8];

                list.add(acidente);

                acidente = null;
            }

            br.close();
            fr.close();
        } catch (IOException e) {
            System.err.println("############ ERRO : " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException nf ) {
            System.err.println("############ ERRO : " + nf.getMessage());
            nf.printStackTrace();
        }
        
    }

    public static List<Acidente> reduzirEscopo(List<Acidente> list) 
    {
        String[] data;
        int mes;

        List<Acidente> collection = new ArrayList<Acidente>();

        for(Acidente a : list) {
            data = (a.dataInversa).split("/");

            mes = Integer.parseInt(data[1]);

            if( mes >= 1 && mes <= 8 ) {
                collection.add(a);
            }
        }

        return collection;
    }

    // Dados mais Gerais
    public static void realizarOperacoes(List<Acidente> list, String ano)
    { 
        // De janeiro á agosto  
        int[] qntMes = new int[8];
        // Meio semana ou final de semana (sexta á domingo)
        int[] periodoSemana = new int[2]; 
        int qntCasos = 0; 
        Double media; // de janeiro á agosto
        String[] data;
        int mes;
        // Agrupar dados por mês 
        for(Acidente a : list) {
            // Extrair mes
            data = (a.dataInversa).split("/");
            
            mes = Integer.parseInt(data[1]); 

            if(a.diaSemana.equals("s�bado") || a.diaSemana.equals("sexta-feira") || a.diaSemana.equals("domingo"))  periodoSemana[1]++;
                else periodoSemana[0]++;

            if( (mes >= 1) && (mes <= 8) ) {
                qntMes[mes-1]++;
                qntCasos++;
            }
        }  

        media = (double) qntCasos/8;

        periodoSemana[1] = periodoSemana[1]/3; // sex, sab, dom
        periodoSemana[0] = periodoSemana[0]/4; // Seg, ter, quar, quin

        try {
            FileWriter fw = new FileWriter("resultados.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("\n======== Dados do ano "+ ano + " ==========\n");
            bw.write("\nQuantidade de casos por mes :\n");
            bw.write("Jan\tFev\tMar\tAbr\tMaio\tJun\tJul\tAgo\n");
            bw.write(qntMes[0]+"\t"+qntMes[1]+"\t"+qntMes[2]+"\t"+qntMes[3]+"\t"+qntMes[4]+"\t"+qntMes[5]+"\t"+qntMes[6]+"\t"+qntMes[7]+"\n");
            bw.write("\nQuantidade de casos médias no ano "+ ano + "\n");
            bw.write(media+"\n");
            bw.write("\nQuantidade de casos médios por dia da semana: \n");
            bw.write("Seg-Qui\tSex-Dom \n");
            bw.write(periodoSemana[0]+"\t\t"+periodoSemana[1] + "\n");

            bw.close();
            fw.close();
        } catch (IOException e) {
            System.err.println(" ######## ERRO "+ e.getMessage());
            e.printStackTrace();
        }
        
    }

    public static void main (String[] args) {
        // Definição de variáveis
        List<Acidente> acidentes2017 = new ArrayList<Acidente>();
        List<Acidente> acidentes2018 = new ArrayList<Acidente>();
        List<Acidente> acidentes2019 = new ArrayList<Acidente>();
        List<Acidente> acidentes2020 = new ArrayList<Acidente>();

        ler(acidentes2017, "2017");
        ler(acidentes2018, "2018");
        ler(acidentes2019, "2019");
        ler(acidentes2020, "2020");

        // Reduzir escopo para meses entre jan - agosto
        acidentes2017 = reduzirEscopo(acidentes2017);
        acidentes2018 = reduzirEscopo(acidentes2018);
        acidentes2019 = reduzirEscopo(acidentes2019);
        acidentes2020 = reduzirEscopo(acidentes2020);

        // Calcular gerar informação a partir dos dados
        realizarOperacoes(acidentes2017, "2017");
        realizarOperacoes(acidentes2018, "2018");
        realizarOperacoes(acidentes2019, "2019");
        realizarOperacoes(acidentes2020, "2020");

    }
}
