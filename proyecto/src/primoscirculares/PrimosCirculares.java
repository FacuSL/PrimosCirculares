package primoscirculares;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/** Estructura que contiene todos los primos circulares menores a un número determinado.
 *  Extiende de ListArray, pero también contiene un TreeMap con los primos circulares
 *  agrupados por dígitos, a fin de poder imprimirlos de una forma más legible.
 *
 * @author Facundo Casares Diaz
 */
public class PrimosCirculares extends ArrayList<Integer>{
    
    //VARIABLES*****************************************************************
    
    /** número maximo que limita hasta donde se calculan los primos circulares */
    private int maximo;
    /** cantidad de hilos que se van a utilizar para el cálculo */
    private int cantidadHilos;
    /** arreglo con los primos necesarios para calcular los primos circulares */
    private ArrayList<Integer> primos;
    /** entero que mantiene el número que se está utilizando para calcular el primo circular */
    private int proximoNumero = 1;
    /** arreglo con los hilos utilizados para calcular los primos circulares */
    private Thread hilos[];
    /** estructura que almacena los primos ordenados por cantidad de dígitos */
    private TreeMap<Integer, TreeMap<Integer,ArrayList<Integer>>> listaPorDigitos;
    
    
    
    
    //CONSTRUCTOR***************************************************************
    
    //--------------------------------------------------------------------------
    /** Constructor: crea un hasMap con todos los primos circulares menores a máximo */
    public PrimosCirculares(int maximo, int cantidadHilos) {
        super();
        //Iniclializa las variables.............................................
        this.maximo = maximo;
        primos = new ArrayList<Integer>();
        listaPorDigitos = new TreeMap();
        
        this.cantidadHilos = cantidadHilos;
        if(cantidadHilos <= 0){
            System.out.println("No puede haber menos de un hilo. Se va a calcular con un hilo.");
            this.cantidadHilos = 1;
        }
        hilos = new Thread[this.cantidadHilos];
        
        //Genera los primos necesarios para nuestros cálculos...................
        generarPrimos(maximo);
        
        //Agregamos los casos especiales para los que no se cumple la regla de que los dígitos sean solo 1,3,7 y 9
        agregarElemento(2);
        agregarElemento(5);
        
        //Crea los hilos necesarios para calcular los primos circulares.........
        for(int i=0; i<this.cantidadHilos; i++){    
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    buscarPrimosCirculares();
                };
            });
            t.start();
            hilos[i] = t;
        }
        
        //Espera a que terminen todos los hilos.................................
        for(int i = 0; i < this.cantidadHilos; i++){
            try {
                hilos[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(PrimosCirculares.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        //Una vez que terrmina todo, ordena la estructura.......................
        Collections.sort(this);
    }
    //--------------------------------------------------------------------------
    
    
    
    
    
    //FUNCIONES PÚBLICAS********************************************************
    
    //--------------------------------------------------------------------------
    /** Escribe el listado de primos, uno abajo del otro.
     * @return (String) listado secuencial de primos circulares
     */
    @Override
    public String toString(){
        int cantidad = size();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<cantidad; i++){
            sb.append("\n").append(get(i));
        }
        return sb.toString();
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /** Escribe el listado separado por dígito agrupnado por rotaciones de cada número.
     * @return (String) listado de primos circulares
     */
    public String toStringPorDigitos(){
        StringBuilder sb = new StringBuilder();
        int cantidadDigitos;
        NavigableSet nav = listaPorDigitos.navigableKeySet();
        Iterator<Integer> it = nav.iterator();
        while(it.hasNext()){
            cantidadDigitos = it.next();
            if(cantidadDigitos == 1){
                //Caso especial.....................................................
                sb.append("\n\nCantidad de Primos Circulares Trviales (con 1 sólo dígito):\n");
            }else{
                sb.append("\n\nCantidad de Primos Circulares con ").append(cantidadDigitos).append(" dígitos:\n");
            }
            
            NavigableSet numeros = listaPorDigitos.get(cantidadDigitos).navigableKeySet();
            Iterator<Integer> itNumeros = numeros.iterator();
            
            
            while(itNumeros.hasNext()){
                int elemento = itNumeros.next();
                sb.append(listaPorDigitos.get(cantidadDigitos).get(elemento));
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    //--------------------------------------------------------------------------
    
    
    
    
    
    
    //FUNCIONES PRIVADAS********************************************************
    
    //--------------------------------------------------------------------------
    /* Función recursiva: obtiene el próximo número candidato a ser primo circular.
     * Para eso, descarta todos aquellos números que cotengan en alguno de sus
     * dígitos, números pares o 5 (esto es porque para ser primo circular, se debe
     * hacer la permutación del número, por lo que, eventualmente, alguna permutación
     * sería divisible por 2 o por 5.
     * Así, solo se consideran números que contengan entre sus dígitos 1, 3, 7 y 9.
     * 
     * @param numero: entero utilizado para armar el próximo número de forma recursiva.
     * @return (int) próximo número compuesto por 1, 3, 7 y 9.
     */
    private int proximo(int numero){
        //Si se acabaron los dígitos, termina la recursión......................
        if(numero == 0){
            return numero;
        } else{ //Si no, elimina los pares y el 5 y forma el número en función del dígito resultante
            int resto = numero / 10;
            int digito = numero % 10;
            if(digito % 2 == 0){
                digito++;
            } if(digito % 5 == 0){
                digito += 2;
            }
            return digito+10*proximo(resto);
        }
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    /** Obtiene de forma síncrona el próximo número candidato a ser primo circular
     */
    private synchronized int getProximoNumero(){
        proximoNumero = proximo(proximoNumero+2);
        return proximoNumero;
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /* Agrega un elemento a la lista y al TreeMap */
    private synchronized void agregarElemento(int numero){
        int hashtag = funcionHash(numero);
        int cantDigitos = cantidadDigitos(numero);
        
        if(!listaPorDigitos.containsKey(cantDigitos)){
            listaPorDigitos.put(cantDigitos, new TreeMap());
        }
        if(listaPorDigitos.get(cantDigitos).get(hashtag) == null){
            listaPorDigitos.get(cantDigitos).put(hashtag, new ArrayList<Integer>());
        }
        listaPorDigitos.get(cantDigitos).get(hashtag).add(numero);
        this.add(numero);
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /** Función que busca los primos circulares desde un conjunto de candidatos
     */
    private void buscarPrimosCirculares(){
        int numero;
        //Mientras existan números formados por dígitos 1,3,7 y 9, busca por números primos circulares
        while((numero = getProximoNumero()) < maximo){
            //Si el número es primo, entonces es candidato para ser primo circular
            if(esPrimo(numero)){
                boolean esPrimoCircular = true;
                ArrayList<Integer> rotaciones = rotacionesDelNumero(numero);
                for(int i=0; i<rotaciones.size(); i++){
                    //Si alguna de las rotaciones no fuera primo, entonces no es primo circular
                    if(!esPrimo(rotaciones.get(i))){
                        esPrimoCircular = false;
                        break;
                    }
                }
                
                /*Si fuera primo circular, entonces los agrega a la estructura.
                 * Solo agrega al número analizado, para poder así aprovechar
                 * el paralelismo de los hilos, y no tener que controlar si el número
                 * ya existe. */
                if(esPrimoCircular){
                    agregarElemento(numero);
                }
            }
        }
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /* Devuelve un hash para un elemento particular, agrupando todos los números
     * de una misma rotación.
     * @param numero: número del que se quiere obtener el valor hash
     * @return (int) valor hash asociado al número
     */
    private int funcionHash(int numero){
        int minimo = maximo;
        //Obtiene las rotaciones, y calcula el hash de la menor de ellas........
        ArrayList<Integer> rotaciones = rotacionesDelNumero(numero);
        rotaciones.add(numero);
        for(int i = 0; i<rotaciones.size(); i++){
            if(rotaciones.get(i) < minimo){
                minimo = rotaciones.get(i);
            }
        }
        int resto = minimo;
        int multiplicar = 1;
        int digito;
        int suma = 0;
        do{
            digito = resto % 10;
            suma += digito*multiplicar;
            multiplicar*=2;
            resto = resto / 10;
        } while(resto>0);
        return suma;
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /** calcula la cantidad de dígitos de un número particular
     */
    private int cantidadDigitos(int numero){
        int resto = numero;
        int cantidad = 0;
        do{
            cantidad++;
            resto = resto / 10;
        } while(resto>0);
        return cantidad;
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /** Indica si un número es primo o no, dividiendo por todos los números primos
     * menor que la raíz cuadrada del número en cuestión.
     * @param numero: número que se quiere determinar si es primo o no
     * @return (boolean) true si el número es primo, y false en caso contrario.
     */
    private boolean esPrimo(int numero){
        double raizCuadrada = Math.sqrt(numero);
        int intRaizCuadrada = (int)raizCuadrada;
        //Si la raíz cuadrada es exacta, ya sabemos que no es primo.............
        if(intRaizCuadrada == raizCuadrada){
            return false;
        } else{ //Si no, divide por todos los primos menores a la raíz..........
            int i = 0;
            int div;
            while(i< primos.size() && (div = primos.get(i)) < raizCuadrada){
                if(numero%div == 0) return false;
                i++;
            }
        }
        return true;
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /** Devuelve en un arreglo todas las permitaciones circulares de un número.
     * @param numero: número para el cuál se quiere calcular las permutaciones
     * @return (ArrayList) arreglo con todos las rotaciones del número
     */
    private ArrayList<Integer> rotacionesDelNumero(int numero){
        ArrayList<Integer> devolver = new ArrayList<Integer>();
        int cantidadDigitos = (""+numero).length()-1;
        double multiplicar = Math.pow(10, cantidadDigitos);
        int ultimoDigito;
        for(int i = 0; i<cantidadDigitos; i++){
            ultimoDigito = numero % 10;
            numero = (int) ((numero / 10) + (ultimoDigito * multiplicar));
            devolver.add(numero);
        }
        return devolver;
    }
    //--------------------------------------------------------------------------
    
    
    
    //--------------------------------------------------------------------------
    /* Para calcular si un número es primo, solo hace falta dividir ese número
     * por todos los números primos anteriores a la raíz del mismo.
     * Por ejemplo, si queremos saber si 1000000 es primo, basta con sacar su raíz
     * cuadrada (o sea, 1000) y dividir 1000000 por todos los primos menores a 1000.
     * @param numeroMaximo: número  que va a denotar hasta qué punto vamos a analizar
     *                      para saber si un número es primo
     * @return (ArrayList) lista con todos los primos menores o iguales a la raíz
     *                     del número pasado por parámetro
     */
    private void generarPrimos(int numeroMaximo){
        
        //Calculamos el límite..................................................
        int limit = (int)Math.sqrt(numeroMaximo);
        
        primos.add(2);
        //Obtenemos todos los primos hasta el límite............................
        for(int i = 3; i< limit; i+=2){
            if(esPrimo(i)){
                primos.add(i);
            }
        }
    }
    //--------------------------------------------------------------------------
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~