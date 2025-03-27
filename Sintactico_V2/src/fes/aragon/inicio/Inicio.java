/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fes.aragon.inicio;

import fes.aragon.codigo.Lexico;
import fes.aragon.codigo.Sym;
import fes.aragon.codigo.Tokens;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author MASH
 */
public class Inicio {
    private boolean error = true;
    private Tokens tokens = null;
    private Lexico analizador = null;

    public static void main(String[] args) {
        Inicio ap = new Inicio();
        BufferedReader buf;
        try {
            buf = new BufferedReader(
                    new FileReader(System.getProperty("user.dir")
                            + "/archivo.txt"));
            ap.analizador = new Lexico(buf);
            ap.S();
            //ap.sentencia();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void S() {
        siguienteToken();
        A();
            if (error==false) {
                if (tokens.getLexema() == Sym.PUNTOCOMA){
                    System.out.println("correcto");
            } else {
                    System.out.println("incorrecto");
                }
            }else{
                System.out.println("Hay errrores");
            }
    }

    private void A() {
        if (tokens.getLexema() == Sym.ENTERO) {
            siguienteToken();
            if (tokens.getLexema() == Sym.MAS) {
                siguienteToken();
                B();
            } else {
                error=true;
                System.out.println("Error A-> se espera un mas");
            }
        } else {
            error=true;
            System.out.println("Error A->se espera un entero");
        }
    }

    private void B() {
        if (tokens.getLexema() == Sym.ENTERO) {
            siguienteToken();
            } else {
               error=true;
               System.out.println("Error B-> Se espera un entero");
            }
    }
    private void sentencia() {
        do {
            this.asignacion();
            if (this.tokens.getLexema() != 2) {
                this.errorSintactico();
                this.siguienteToken();
            }

            if (!this.error) {
                System.out.println("Invalida linea= " + (this.tokens.getLinea() + 1));
                this.error = true;
            } else {
                System.out.println("Valida  linea= " + (this.tokens.getLinea() + 1));
            }

            this.siguienteToken();
        } while(this.tokens.getLexema() != 4);

    }

    private void asignacion() {
        if (this.tokens.getLexema() == 1) {
            this.siguienteToken();
            if (this.tokens.getLexema() == 5) {
                this.siguienteToken();
                this.expresion();
            } else {
                this.errorSintactico();
            }
        } else {
            this.errorSintactico();
        }

    }

    private void expresion() {
        if (this.tokens.getLexema() != 1 && this.tokens.getLexema() != 0) {
            if (this.tokens.getLexema() != 2) {
                this.errorSintactico();
            }
        } else {
            this.siguienteToken();
            if (this.tokens.getLexema() != 6 && this.tokens.getLexema() != 7) {
                if (this.tokens.getLexema() != 2) {
                    this.errorSintactico();
                }
            } else {
                this.siguienteToken();
                this.expresion();
            }
        }

    }

    private void errorSintactico() {
        this.error = false;
        //descartar todo hasta encontrar ;            
        do {
            System.out.println(tokens.toString());
            if (tokens.getLexema() != Sym.PUNTOCOMA) {
                siguienteToken();
            }
        } while (tokens.getLexema() != Sym.PUNTOCOMA && tokens.getLexema() != Sym.EOF);

    }

    private void siguienteToken() {
        try {
            tokens = analizador.yylex();
            if (tokens == null) {
                tokens = new Tokens("EOF", Sym.EOF, 0, 0);
                throw new IOException("Fin Archivo");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
