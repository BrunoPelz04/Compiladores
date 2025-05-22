package mx.unam.aragon.interprete;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CompiladorController {

    private GraphicsContext graficos;
    private Scene escena;
    private AnimationTimer tiempo;
    private ArrayList<String> comandos = new ArrayList<>();

    //coordenadas
    private int x=0;
    private int y=0;
    private int ancho=0;
    private int alto=0;
    //banderas
    boolean fondoActivo = false;
    boolean posicionPunto=false;
    boolean lineaActiva=false;
    boolean limpiar=false;
    //variables especiales
    Color color = null;
    Color colorFigura=null;
    @FXML
    private Button bntEjecutar;

    @FXML
    private Button btnCompilar;

    @FXML
    private Canvas canvas;

    @FXML
    private TextArea txtCodigo;

    @FXML
    private TextArea txtMensajes;

    @FXML
    void actionCompilar(ActionEvent event) {

    }

    @FXML
    void actionEjecutar(ActionEvent event) {
        this.leerArchivo();
        this.lecturaComando();
        this.iniciar();
    }

    public void setEscena(Scene escena) {
        this.escena = escena;
    }

    public void iniciar() {
        componentesIniciar();
        //cerrarJuego();
        ciclo();

    }

    private void pintar() {
        if (fondoActivo) {
            graficos.setFill(color);
            graficos.fillRect(0, 0, 400, 400);
            fondoActivo=false;
        }
        if(limpiar){
            graficos.clearRect(0,0,400,400);
            limpiar=false;
        }
        if(posicionPunto){
            graficos.setFill(Color.BLACK);
            graficos.fillOval(x,y,5,5);
        }
        if(lineaActiva){
            graficos.setStroke(colorFigura);
            graficos.strokeLine(x,y,ancho,alto);
            lineaActiva=false;
        }




    }
    private void leerArchivo() {
        comandos.add("lpr");
        comandos.add("f,rojo");
        comandos.add("ps,150,150");
        comandos.add("lin,20,20,verde");
        comandos.add("ps,50,70");
        comandos.add("lin,120,20,azul");
        comandos.add("ps,51,71");
        comandos.add("lin,121,21,azul");
    }

    private void lecturaComando() {
        String[] comando = comandos.remove(0).split(",");
        System.out.println("Comando");
        //comando de color de fondo
        if (comando[0].equals("f")) {
            fondoActivo = true;
            if (comando[1].equals("rojo")) {
                this.color = Color.RED;
            } else if (comando[1].equals("azul")) {
                this.color = Color.BLUE;
            } else if (comando[1].equals("verde")) {
                this.color = Color.GREEN;
            }
        }

        //comando limpiar
        if (comando[0].equals("lpr")) {
            limpiar = true;
        }
        //
        if(comando[0].equals("ps")){
            this.x=Integer.parseInt(comando[1]);
            this.y=Integer.parseInt(comando[2]);
            posicionPunto=true;
        }
        //comando linea
        if (comando[0].equals("lin")) {
            this.ancho=Integer.parseInt(comando[1]);
            this.alto=Integer.parseInt(comando[2]);
            if (comando[3].equals("rojo")) {
                this.colorFigura = Color.RED;
            } else if (comando[3].equals("azul")) {
                this.colorFigura = Color.BLUE;
            } else if (comando[3].equals("verde")) {
                this.colorFigura = Color.GREEN;
            }
            lineaActiva=true;
        }

        //terminar si ya no hay comandos
        if(comandos.isEmpty()){
            System.out.println("Mato");
            tiempo.stop();
        }
    }

    private void cerrarJuego() {
        Stage stage = (Stage) canvas.getScene().getWindow();
        stage.setOnCloseRequest((t) -> {
                    tiempo.stop();
                    stage.close();
                }
        );
    }

    private void componentesIniciar() {
        graficos = canvas.getGraphicsContext2D();
    }

    private void ciclo() {
        final long[] tiempoInicio = {System.nanoTime()};
        tiempo = new AnimationTimer() {
            @Override
            public void handle(long tiempoActual) {
                double t = (tiempoActual - tiempoInicio[0]) / 1000000000.0;
                System.out.println((int)t%5);
                if((int)t%5==1){
                    tiempoInicio[0] = System.nanoTime();
                    lecturaComando();
                }
                pintar();

            }

        };
        tiempo.start();
    }



}
