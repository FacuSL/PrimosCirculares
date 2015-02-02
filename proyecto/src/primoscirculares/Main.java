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
     * @param args: En el primer argumento se determinan la cantidad de hilos para
     *              calcular los primos; si no se escribeo falla, utiliza los que tiene por defecto.
     *              En el segundo argumento se determina la forma de mostrar el resultado.
     *              Si dice agrupado, la lista de primos circulares se muetra separada por
     *              dígitos y se agrupan los números que son rotaciones entre si.
     *              Si no dice nada, se muestra como lista secuencial ordenada.
     */
    public static void main(String[] args) {
        int cantidadHilos = 4;
        boolean agrupado = false;
        
        //Almacena el tiempo al inicio de la ejecución..........................
        long startTime = System.currentTimeMillis();
        
        //Intenta leer la cantidad de hilos. Si no está determinada u ocurre un error, usa los que vienen por defecto.
        if (args.length>0){
            try{
                cantidadHilos = Integer.valueOf(args[0]);
            } catch(Exception e){
                cantidadHilos = 4;
                System.out.println("Error la cantidad de hilos. Se van a calcular los primos circulares usando "+cantidadHilos+" hilos.");
            }
        }
        
        //Intenta leer la forma en la que se debe mostrar el resultado. Si no está determinada u ocurre un error, muestra como lista.
        if (args.length>1){
            try{
                if(args[1].toLowerCase().equals("agrupado")){
                    agrupado = true;
                } else{
                    System.out.println("Parámetro mal escrito. Se va a mostrar el resultado como lista.");
                }
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
