import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class HundirLaFlotaMain {

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int contadorPartidas = 0;
            char continua = 'a';

            do {// para saber si el jugador quiere seguir jugando después de acabar usaremos el bucle do-while para que al menos se inicie el juego una vez y después dependa de lo que intruduzca el usuario
                System.out.println("HUNDE LA FLOTA"); // título
                System.out.println("¿Qué modo desea jugar (básico o avanzado)?"); // para elegir el modo básico o avanzado
                String nivel;
                nivel = sc.nextLine();

                if (nivel.contains("v")) {
                    System.out.println("Ha elegido el modo avanzado");
                    System.out.println("¿Cuantos desean jugar (1 jugador o 2 jugadores)?"); // solo en el método avanzado existe la posibilidad de dos jugadores como se especifica en el enunciado
                    String jugadores = sc.nextLine();

                    if(jugadores.equals("1")) {
                        modoAvanzado(); // si elige el modo un jugador, comienza el método modo avanzado de forma normal
                    }
                    else if(jugadores.equals("2")) {
                        modoDosJugadores(); // como se han elegido dos jugadores se usa este método, que incluye los turnos de cada jugador
                    }
                    else {
                        System.out.println("Error en la elección de jugadores"); 
                        jugadores = sc.nextLine();
                    }

                }
                else if (nivel.contains("b")) { // si escribe la palabra básico se inicia el método con el modo básico
                    System.out.println("Ha elegido el modo básico");
                    modoBasico();
                } else {
                    System.out.println("No ha elegido ni el modo básico ni el avanzado, vuelva a escribir uno de ellos.");
                    nivel = sc.nextLine();
                }
                
                // para contar la cantidad de partidas que lleva el jugador cuando finaliza la que acaba de realizar y para decidir si quiere jugar nuevamente
                contadorPartidas++;
                System.out.println("Lleva " + contadorPartidas + " partidas");
                System.out.println("¿Desea jugar nuevamente (S/N)?");
                continua = sc.nextLine().charAt(0);
            
            } while (continua == 'S' || continua == 's' ); // si el usuario decide continuar se vuelve a comenzar el bucle, sino acaba el juego
            
            System.out.println("El juego ha finalizado completamente.");

        }

        // modo básico del juego
        public static void modoBasico () {
            String [] [] tablero = new String[5][5]; // para el modo básico se crean dos tableros 5x5, el que el usuario ve y el oculto con los barcos
            String [] [] tablerobarcos= new String[5][5];
            
            // se crean dos tableros principales: el que el usuario ve y el oculto con los barcos
            inicializarTablero(tablero);// el primer tablero que ve el usuario
            inicializarTablero(tablerobarcos);

            // colocar los 4 barcos en el tablero 5x5
            colocarBarcos(tablerobarcos, 2, 0); // tablero, longitud del barco, direccion (horizontal es 0 y vertical 1)
            colocarBarcos(tablerobarcos, 2, 1);
            colocarBarcos(tablerobarcos, 3, 0);
            colocarBarcos(tablerobarcos, 3, 1);

            // los simbolos usados se especifican en el enunciado
            String simboloTocado = "#";
            String simboloAgua = "0";
            String simboloHundido= "X";
            
            // inicio de la cantidad de disparos y barcos
            int barcosPorHundir = 4;
            int barcosHundidos = 0;
            int disparosRealizados =0;

            // ciclos del juego
            while (barcosHundidos<4) {
                mostrarTablero(tablero); // se muestra el tablero aquí para saber que los barcos se han colocado
           
                int [] coordenadas = obtenerCoordenadasValidas(tablero.length, tablero[0].length);

                // para extraer las coordenadas válidas
                int posx = coordenadas[0]; // coordenadas de la fila
                int posy = coordenadas[1]; // coordenadas de la columna

                // procesar el disparo, hundido, agua o tocado
                if (tablerobarcos[posx][posy].equals("□")) {
                    tablero[posx][posy] = simboloAgua;
                    tablerobarcos[posx][posy] = simboloAgua;
                    disparosRealizados++;
                    System.out.println("Has fallado. ¡Agua!");

                } else if (tablerobarcos[posx][posy].equals(simboloHundido) || tablerobarcos[posx][posy].equals(simboloAgua) || tablerobarcos[posx][posy].equals(simboloTocado)) {
                    System.out.println("Ya has disparado esa coordenada"); // si en la coordenada elegida hay cualquier otro símbolo que no sea □ significa que ya se ha disparado
                    disparosRealizados++;

                } else { // cuando es un barco 
                    if (tablerobarcos[posx][posy].equals("B")) {
                        tablero[posx][posy] = simboloTocado;
                        tablerobarcos[posx][posy] = simboloTocado; // marcar el impacto de tocado
                        System.out.println("¡Has tocado!");
                        disparosRealizados++;
                    }
                    
                    // validar cuando se hunde el barco por completo
                    if (hundido(tablerobarcos, posx, posy, 2, simboloTocado)) { 
                    	barcosHundidos++;
                    	barcosPorHundir--;
                    	for (int i = 0; i <tablero.length; i++) { // para cambiar el simbolo tocado por hundido
                            for (int j = 0; j<tablero.length; j++) {
                               if (tablero[i][j] == simboloTocado) {
                            	   simboloTocado = simboloHundido;
                            	   tablero[i][j] = simboloTocado;}
                            }
                        } 
                    }
                    else if (hundido(tablerobarcos, posx, posy, 3, simboloTocado)) { 
                    	barcosHundidos++;
                    	barcosPorHundir--;
                    	for (int i = 0; i <tablero.length; i++) { // para cambiar el simbolo tocado por hundido
                            for (int j = 0; j<tablero.length; j++) {
                               if (tablero[i][j] == simboloTocado) {
                            	   simboloTocado = simboloHundido;
                            	   tablero[i][j] = simboloTocado;}
                            }
                        } 
                    }

                }

                // actualización del tablero con las jugadas realizadas
                System.out.println("Disparos realizados: " + disparosRealizados);
                System.out.println("Barcos hundidos: " + barcosHundidos + "/ Barcos por hundir: " + barcosPorHundir);
            }

            // Fin del juego
            System.out.println("\n ¡Felicidades! Has conseguido hundir todos los barcos.");
            reset(tablero, tablerobarcos, 4, 0);

        }

        // el modo avanzado del juego
        public static void modoAvanzado () {
        	
            // para que seleccione el tamaño del tablero
            System.out.println("Seleccione el tamaño del tablero, del 5 al 10");
            Scanner bacteria = new Scanner(System.in);
            int gridSize = Integer.parseInt(bacteria.nextLine());
            
            // se crean dos tableros principales: el que el usuario ve y el oculto con los barcos
            String [][] tablero = new String[gridSize][gridSize];
            String [][] tablerobarcos = new String[gridSize][gridSize];

            // inicializar los tableros y rellenarlos según el tamaño elegido por el usuario
            inicializarTablero(tablero);
            inicializarTablero(tablerobarcos);

            // colocar los 4 barcos en el tablero en direccion horizontal (0) y vertical (1)
            colocarBarcos(tablerobarcos, 2, 0);  // tablero, longitud del barco, direccion
            colocarBarcos(tablerobarcos, 2, 1);
            colocarBarcos(tablerobarcos, 3, 0);
            colocarBarcos(tablerobarcos, 3, 1);

            // las coordenadas a introducir por el usuario tras haber colocado los barcos
            System.out.println("Introduce un símbolo para tocado:");
            String simboloTocado = bacteria.nextLine();
            System.out.println("Introduce un símbolo para agua:");
            String simboloAgua = bacteria.nextLine();
            System.out.println("Introduce un símbolo para hundido:");
            String simboloHundido= bacteria.nextLine();


            int barcosPorHundir = 4; //el número inicial de barcos por hundir
            int barcosHundidos = 0;// el número inicial de barcos hundidos
            int disparosRealizados =0; // el número inicial de disparos realizados

            // ciclos del juego
            while (barcosHundidos<4) {
                mostrarTablero(tablero); // se muestra el tablero en el bucle while para saber que los barcos se han colocado correctamente en el tablero oculto

                int [] coordenadas = obtenerCoordenadasValidas(tablerobarcos.length, tablerobarcos[0].length);

                // para extraer las coordenadas válidas
                int posx = coordenadas[0]; // la coordenada de los números
                int posy = coordenadas[1]; // la coordenada de las letras

                // procesar el disparo, hundido, agua o tocado
                if (tablerobarcos[posx][posy].equals("□")) {
                    tablero[posx][posy] = simboloAgua;
                    tablerobarcos[posx][posy] = simboloAgua;
                    disparosRealizados++;
                    System.out.println("Has fallado. ¡Agua!");

                } else if (tablerobarcos[posx][posy].equals(simboloHundido) || tablerobarcos[posx][posy].equals(simboloAgua) || tablerobarcos[posx][posy].equals(simboloTocado)) {
                    System.out.println("Ya has disparado esa coordenada"); // si el usuario vuelve a introducir la misma coordenada
                    disparosRealizados++;

                } else { // cuando es un barco
                    if (tablerobarcos[posx][posy].equals("B")) {
                        tablero[posx][posy] = simboloTocado;
                        tablerobarcos[posx][posy] = simboloTocado; // para cambiar de la casilla vacía o del barco al simbolo tocado
                        disparosRealizados++; // marcar el impacto
                        System.out.println("¡Has tocado!");

                        // validar cuando se hunde el barco por completo
 
                        if ((hundido(tablerobarcos, posx, posy, 2, simboloTocado))) { 
                        	barcosHundidos++;
                        	barcosPorHundir--;
                        	for (int i = 0; i <tablero.length; i++) { // para cambiar el simbolo tocado por hundido
                                for (int j = 0; j<tablero.length; j++) {
                                   if (tablero[i][j] == simboloTocado) {
                                	   simboloTocado = simboloHundido;
                                	   tablero[i][j] = simboloTocado;}
                                }
                            } 
                        	
                        }
                        else if (hundido(tablerobarcos, posx, posy, 3, simboloTocado)) { 
                        	barcosHundidos++;
                        	barcosPorHundir--; // como es hundido se quita uno y se añade al de barcos hundidos
                        	for (int i = 0; i <tablero.length; i++) { // para cambiar el simbolo tocado por hundido
                                for (int j = 0; j<tablero.length; j++) {
                                   if (tablero[i][j] == simboloTocado) {
                                	   simboloTocado = simboloHundido;
                                	   tablero[i][j] = simboloTocado;}
                                }
                            } 
                        }
                    }

                }

                // actualización del tablero con las jugadas realizadas
                System.out.println("Disparos realizados: " + disparosRealizados);
                System.out.println("Barcos hundidos: " + barcosHundidos + "/ Barcos por hundir: " + barcosPorHundir);
                // para que el usuario tenga en cuenta todos los disparos realizados, los barcos que ha hundido y los que quedan
            }

            // Fin del juego
            System.out.println(" "); // para dejar un espacio en el final del juego 
            System.out.println("\n ¡Felicidades! Has conseguido hundir todos los barcos.");
            reset(tablero, tablerobarcos, 4 , 0); // se resetea todo el juego

        }

        // modo dos jugadores
        public static void modoDosJugadores() { //en vez de crear todo el codigo completo para dos jugadores
        	// se opta por crear un nuevo método en el que se juega por turnos
        	// cuando le toca al primer jugador se inicia el modo avanzado para ese jugador y cuando le toque al segundo jugador sucede lo mismo
            int turno = 0; // para diferenciar cuando le toca a cada jugador

            while (true) {
                if (turno == 0) { // para turno = 0 juega el primer jugador
                    System.out.println("Jugador 1");
                    modoAvanzado();
                    turno = 1; // para que el siguiente turno sea del jugador 2

                } else {//if (turno == 1) juega el jugador 2
                    System.out.println("Jugador 2");
                    modoAvanzado();
                    turno = 0; // el turno cambia al jugador 1
                }
            }
        }

        // para inicializar el tablero
        public static void inicializarTablero (String [][] tablero) {
            for (int i = 0; i <tablero.length; i++) {
                for (int j = 0; j<tablero.length; j++) {
                    tablero[i][j] = "□";
                }
            }
        }

        // para mostrar el tablero con letras y números para las coordenadas
        public static void mostrarTablero (String [][] tablero) {
            int gridSize = tablero.length;

            // encabezado de las letras en las columnas
            // Espacio para la esquina superior
            System.out.print("  ");
            for (int i = 0; i < tablero[0].length; i++) { // para poner el encabezado de las letras
                System.out.print((char) ('A' + i) + " "); 
            }
            System.out.println();

            //contenido del tablero con números en las filas
            for (int i = 0; i < gridSize; i++) {
                System.out.print(i + " "); // número de fila alineado a la derecha
        
                	for (int j = 0; j < gridSize; j++) {
                		System.out.print(tablero[i][j] + " ");
                	}
                System.out.println();
            }
            System.out.println();
        }

        // imprimir el tablero se usa este método para comprobar que se colocan bien los barcos y otras opciones 
        public static void printTablero (String [][] tablero) {
            for (int i = 0; i <tablero.length; i++) {
                for (int j = 0; j<tablero.length; j++) {
                    System.out.print(tablero[i][j] + " ");
                }
                System.out.println();
            }
        }

        // método para colocar los barcos
        public static void colocarBarcos(String[][] tablerobarcos, int longitudBarco, int direccion) {
            Random random = new Random(); // para generar la posición aleatoria de los barcos
            boolean colocado = false;
            int longitudTablero = tablerobarcos.length;

            while (!colocado) { // repetir hasta que el barco se coloque bien
                int x = random.nextInt(longitudTablero); // Random con la coordenada x -- columnas
                int y = random.nextInt(tablerobarcos[0].length); // Random con la coordenada y -- filas
               
                if (puedoColocarBarco(tablerobarcos, x, y, longitudBarco, direccion)) { // si se cumple esta condición se colocan los barcos en el tablero oculto
                    for (int i = 0; i < longitudBarco; i++) {
                        if (direccion == 0) { // Horizontal -- barco
                            tablerobarcos[x][y + i] = "B";
                        } else { // Vertical  -- barco direccion == 1
                            tablerobarcos[x + i][y] = "B";
                        }
                    }
                    colocado = true;  // acabar el bucle cuando el barco esté bien colocado
                }
            }
        }

        // validar si es posible colocar los barcos
        public static boolean puedoColocarBarco(String[][] tableroBarcos, int x, int y, int longitudBarco, int direccion) {
         
            if (direccion == 0) { // Horizontal
                if (y + longitudBarco > tableroBarcos[0].length) return false; // fuera de los límites --> si la posición más la longitud del barco es mayor a la longitud del tablero
                	for (int i = 0; i < longitudBarco; i++) {// recorre solo hasta la longitud del barco porque solo se necesita saber si las siguientes casillas están ocupadas, no hace falta recorrer todo el tablero
                		if (tableroBarcos[x][y + i].equals("B")) return false; // casilla ocupada 
                }
            } else { // Vertical -- if (direccion == 1)
                if (x + longitudBarco > tableroBarcos.length) return false; // fuera de los límites --> si la posición más la longitud del barco es mayor a la longitud del tablero
                	for (int i = 0; i < longitudBarco; i++) {
                		if ((tableroBarcos[x + i][y].equals("B"))) return false; // casilla ocupada
                }
            }
            return true; // si no se cumple ninguna de las anteriores condiciones se puede colocar el barco en esa posición
        }

        // validar y obterner las coordenadas introducidas por el usuario
        public static int[] obtenerCoordenadasValidas (int posxMax, int posyMax) {
            Scanner sc = new Scanner(System.in);
            boolean valido = false;
            int [] coordenada = new int [2]; // se crea un string array con las dos coordenadas posx y posy 

            while (!valido) { // se usa este bucle para asegurar que mientras las coordenadas no sean válidas se repita
                System.out.println("Ingrese las coordenadas (ejemplo: 1 B):");
                String entrada = sc.nextLine().toUpperCase();

                try {
                    int posx = Integer.parseInt(entrada.split(" ")[0]); // fila
                    char columnaChar = entrada.split(" ")[1].charAt(0); // columna

                    System.out.println("Las coordenadas introducidas son: " + "[" + posx + "," + columnaChar + "]");
                    int posy = columnaChar - 'A'; // para convertir la letra de columna a número

                    // para verificar que estén dentro de los límites del tablero
                    if (posx >= 0 && posx< posxMax && posy >= 0 && posy < posyMax) {
                        coordenada[0] = posx; // el array de coordenadas se rellena con las posiciones introducidas por el usuario
                        coordenada[1] = posy;
                        valido = true; // las coordenadas son válidas
                        
                    } else {
                        System.out.println("Coordenadas fuera de los límites");
                    }

                } catch (InputMismatchException e) { // si el usuario ingresa algo que no es un número entero, se muestra el siguiente mensaje
                    System.out.println("Las coordenadas no son válidas.");
                    sc.nextLine(); //para que vuelva a ingresar las coordenadas
                }
            }
            return coordenada;
        }

        // para reiniciar el juego y el contador de partidas
        public static String [][] reset (String [][] tablero, String [][] tablerobarcos, int barcosPorHundir, int barcosHundidos) {
            Scanner sc = new Scanner(System.in);
            int contadorPartidas = 0;

            while (barcosPorHundir == 0) { // mientras la cantidad de barcos por hundir sea 0, el juego se ha acabado y se resetea todo como en el inicio
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero.length; j++) {
                        tablero[i][j] = "□"; // volver a rellenar los tableros aunque técnicamente no hace falta, ya que se inicializan y rellenan al principio de cada modo
                        tablerobarcos [i][j] = "□";
                        barcosHundidos = 0;
                        barcosPorHundir = 4;

                        if (tablero[i][j] == "□") {
                            contadorPartidas = contadorPartidas + 1; // para contar la cantidad de partidas jugadas
                            System.out.println(contadorPartidas + ": juegos realizados.");
                        } else {
                            System.out.println(contadorPartidas + ": juegos realizados.");
                        }
                    }
                }
                return tablero;
            }
            return tablero;
        }

  // en este último método se describe cuando un barco está hundido
        public static boolean hundido(String[][] tableroBarcos, int posx, int posy, int longitudBarco, String simboloTocado) {
            // Comprobar si el barco es horizontal
            boolean esHorizontal = true;
            for (int i = 0; i < longitudBarco; i++) {
                if (posy + i >= tableroBarcos[0].length || (!tableroBarcos[posx][posy + i].equals("B") && !tableroBarcos[posx][posy].equals(simboloTocado))) {
                    esHorizontal = false; // el barco no es horizontal
                    break;
                }
            } // se usa el bucle for para recorrer las casillas que se encuentran al lado de la coordenada introducida

            // Comprobar si el barco es vertical
            boolean esVertical = true;
            for (int i = 0; i < longitudBarco; i++) { // recorre solo la longitud del barco para comprobar cómo es
                if (posx + i >= tableroBarcos.length || (!tableroBarcos[posx + i][posy].equals("B") && !tableroBarcos[posx][posy].equals(simboloTocado))) {
                    esVertical = false; // el barco no es vertical
                    break;
                }
            }

         // Verificar si todas las partes del barco están tocadas
            if (esHorizontal) {
                // Verificar a la derecha 
                for (int j = posy; j < tableroBarcos[0].length && (tableroBarcos[posx][j].equals("B") || tableroBarcos[posx][j].equals(simboloTocado)); j++) {
                	//tableroBarcos[0].length --> esto significa que j sea menor a la longitud de la primera fila
                	if (tableroBarcos[posx][j].equals("B")) return false; // Parte del barco intacta
                }
                // Verificar a la izquierda
                for (int j = posy; j >= 0 && (tableroBarcos[posx][j].equals("B") || tableroBarcos[posx][j].equals(simboloTocado)); j--) {
                    if (tableroBarcos[posx][j].equals("B")) return false; // Parte del barco intacta
                }
            } else {
                // Verificar hacia abajo
                for (int i = posx; i < tableroBarcos.length && (tableroBarcos[i][posy].equals("B") || tableroBarcos[i][posy].equals(simboloTocado)); i++) {
                    if (tableroBarcos[i][posy].equals("B")) return false; // Parte del barco intacta
                }
                // Verificar hacia arriba
                for (int i = posx; i >= 0 && (tableroBarcos[i][posy].equals("B") || tableroBarcos[i][posy].equals(simboloTocado)); i--) {
                    if (tableroBarcos[i][posy].equals("B")) return false; // Parte del barco intacta
                }
            }

            // Si todas las celdas del barco están marcadas como tocadas
            System.out.println("¡Hundido!");
            return true;
        }

}
