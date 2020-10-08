/**
 * @author Diogo Araujo Miranda
 */
class Acidente {
    public long id;
    public String dataInversa;
    public String diaSemana;
    public String horario;
    public String uf;
    public String municipio;
    public String causaAciente;
    public String faseDia;
    public String condicaoMeteologica;

    public void mostrar() {
        System.out.println ( "[ "+ id + " " + dataInversa + " " + diaSemana + " " + horario + " "
                                      + uf + " " + municipio + " " + causaAciente + " " + faseDia + " " + condicaoMeteologica + " ]"); 
    }
}