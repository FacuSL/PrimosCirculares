package primoscirculares;

/**
 *
 * @author Facundo Casares Diaz
 */
public class Main {
    
    static int max = 1000000;
    
    
    //--------------------------------------------------------------------------
    /** Programa Principal. Crea una instancia de PrimosCirculares, que se encarga
     *  de realizar las operaciones necesarias.
     * @param args: En el primer argumento se determina si la lista se va a mostrar agrupada
     *              por cantidad de dígitos o no. Si no dice nada, se muestra como lista.
     */
    public static void main(String[] args) {
        int cantidadHilos = 4;
        boolean agrupado = false;
        
        //Almacena el tiempo al inicio de la ejecución..........................
        long startTime = System.currentTimeMillis();
        
        //Intenta leer la cantidad de hilos. Si no está determinada u ocurre un error, usa los que vienen por defecto.
        if (args.length>0){
            try{
                agrupado = args[0].equals("agrupado");
            } catch(Exception e){
                System.out.println("Error al leer el parámetro. Se va a mostrar el resultado como lista.");
            }
        }
        
        //Calcula todos los primos circulares menores a max.....................
        PrimosCirculares primosCirculares = new PrimosCirculares(max, cantidadHilos);
        
        //Almacenamos el tiempo al final de la ejecución........................
        long endTime   = System.currentTimeMillis();
        
        //Calcula el total del tiempo...........................................
        long totalTime = endTime - startTime;
        
        System.out.println("Tiempo de ejecución: "+totalTime+" milisegundos");
        System.out.println("Cantidad de Primos Ciruculares: "+primosCirculares.size());
        
        //Escribe el resultado
        if(agrupado){
            System.out.println("Listado agrupados por dígitos: "+primosCirculares.toStringPorDigitos());
        } else{
            System.out.println("Listado: "+primosCirculares);
        }
    }
    
    
}
