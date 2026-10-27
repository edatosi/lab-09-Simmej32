package listas;

import java.util.NoSuchElementException;

/**
 * Lista simplemente enlazada genérica.
 *
 * INSTRUCCIONES PARA EL ESTUDIANTE
 * --------------------------------
 * 1. Cada método marcado con "TODO" lanza UnsupportedOperationException.
 *    Reemplace esa línea por su implementación.
 * 2. NO modifique las pruebas (src/test). 
 * 3. Resuelva en orden (Etapa 1, 2, 3...): cada etapa usa las anteriores.
 * 4. Solo se permite usar nodos propios (clase Nodo). No use
 *    java.util.LinkedList ni ArrayList dentro de esta clase.
 */
public class ListaEnlazada<T> {

    /** Primer nodo de la lista (null cuando la lista está vacía). */
    private Nodo<T> cabeza;

    /** Cantidad de elementos. Debe mantenerse actualizada en cada operación. */
    private int cantidad;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cantidad = 0;
    }

    // =====================================================================
    // ETAPA 1. Estado de la lista
    // =====================================================================

    /** @return true si la lista no tiene elementos. */
    public boolean estaVacia() {
        return cantidad == 0;
    }

    /** @return número de elementos de la lista. */
    public int tamanio() {
        return cantidad;
    }

    // =====================================================================
    // ETAPA 2. Agregar en los extremos
    // =====================================================================

    /** Inserta el dato como nuevo primer elemento. */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(cabeza); 
        cabeza = nuevoNodo;
        cantidad++;
    }

    /** Inserta el dato como nuevo último elemento. */
    public void agregarAlFinal(T dato) {
        if (estaVacia()) {
            agregarAlInicio(dato);
            return;
        }
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        Nodo<T> actual = cabeza;

        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        actual.setSiguiente(nuevoNodo);
        cantidad++;
    }

    // =====================================================================
    // ETAPA 3. Consultar elementos
    // =====================================================================

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerPrimero() {
        if (estaVacia()) throw new NoSuchElementException("Lista vacía");
        return cabeza.getDato();
    }

    /** @throws NoSuchElementException si la lista está vacía */
    public T obtenerUltimo() {
        if (estaVacia()) throw new NoSuchElementException("Lista vacía");
        
        Nodo<T> actual = cabeza;
        // Recorre hasta detenerse en el último vagón
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     * @param indice posición (0 = primer elemento)
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= cantidad) throw new IndexOutOfBoundsException("Índice no válido");
        
        Nodo<T> actual = cabeza;
        // Da saltos exactos hasta la posición solicitada
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // =====================================================================
    // ETAPA 4. Búsqueda
    // =====================================================================

    /** @return true si algún elemento es igual (equals) al dato. Admite null. */
    public boolean contiene(T dato) {
        return indiceDe(dato) != -1;
    }

    /** @return posición de la primera aparición del dato, o -1 si no está. */
    public int indiceDe(T dato) {
        Nodo<T> actual = cabeza;
        int indice = 0;

        // Recorremos todos los vagones hasta que se acabe el tren (actual sea null)
        while (actual != null) {
            // Objects.equals compara de forma segura, incluso si los datos son null
            if (java.util.Objects.equals(actual.getDato(), dato)) {
                return indice;
            }
            actual = actual.getSiguiente();
            indice++;
        }
        return -1;
    }

    // =====================================================================
    // ETAPA 5. Insertar y reemplazar en cualquier posición
    // =====================================================================

    /**
     * Inserta el dato en la posición indicada, desplazando los siguientes.
     * Se permite indice == tamanio() (equivale a agregar al final).
     *
     * @throws IndexOutOfBoundsException si indice < 0 o indice > tamanio()
     */
    public void insertar(int indice, T dato) {
        if (indice < 0 || indice > tamanio()){
            throw new IndexOutOfBoundsException("Índice no válido");
        }
        if (indice == 0) {
            agregarAlInicio(dato);
            return;
        }
        Nodo<T> anterior = cabeza;
        for (int i = 0; i < indice - 1; i++) {
            anterior = anterior.getSiguiente();
        }
        // Conectamos el nuevo vagón sin romper el tren
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(anterior.getSiguiente());
        anterior.setSiguiente(nuevoNodo);
        cantidad++;
    }

    /**
     * Reemplaza el dato de la posición indicada.
     *
     * @return el dato que había antes
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T reemplazar(int indice, T dato) {
        if (indice < 0 || indice >= cantidad) throw new IndexOutOfBoundsException("Índice no válido");

        // Avanzamos hasta el vagón exacto
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }

        // Guardamos la carga vieja, metemos la nueva y retornamos la vieja
        T datoAntiguo = actual.getDato();
        actual.setDato(dato);
        return datoAntiguo;
    }

    // =====================================================================
    // ETAPA 6. Eliminar
    // =====================================================================

    /**
     * Elimina y retorna el primer elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarPrimero() {
        if (estaVacia()) throw new NoSuchElementException("Lista vacía");
        
        T dato = cabeza.getDato();
        // La cabeza salta al segundo vagón, desconectando el primero
        cabeza = cabeza.getSiguiente(); 
        cantidad--;
        return dato;
    }

    /**
     * Elimina y retorna el último elemento.
     * @throws NoSuchElementException si la lista está vacía
     */
    public T eliminarUltimo() {
        if (estaVacia()) throw new NoSuchElementException("Lista vacía");
        if (cantidad == 1) return eliminarPrimero(); // Caso de un solo vagón

        Nodo<T> penultimo = cabeza;
        // Avanzamos hasta detenernos exactamente en el penúltimo vagón
        while (penultimo.getSiguiente().getSiguiente() != null) {
            penultimo = penultimo.getSiguiente();
        }

        T dato = penultimo.getSiguiente().getDato();
        // Desconectamos el último vagón haciendo que el penúltimo apunte a null
        penultimo.setSiguiente(null); 
        cantidad--;
        return dato;
    }

    /**
     * Elimina y retorna el elemento de la posición indicada.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= tamanio()
     */
    public T eliminar(int indice) {
        if (indice < 0 || indice >= cantidad) throw new IndexOutOfBoundsException("Índice no válido");
        if (indice == 0) return eliminarPrimero();

        Nodo<T> anterior = cabeza;
        // Nos detenemos en el vagón anterior para poder puentear el enlace
        for (int i = 0; i < indice - 1; i++) {
            anterior = anterior.getSiguiente();
        }

        T dato = anterior.getSiguiente().getDato();
        // Saltamos el vagón que queremos borrar, conectando directo con el que le sigue
        anterior.setSiguiente(anterior.getSiguiente().getSiguiente());
        cantidad--;
        return dato;
    }

    /**
     * Elimina la primera aparición del dato.
     * @return true si se eliminó algo, false si el dato no estaba
     */
    public boolean eliminarValor(T dato) {
        // Reutilizamos la lógica que ya hicimos en las etapas anteriores
        int indice = indiceDe(dato);
        if (indice != -1) {
            eliminar(indice);
            return true;
        }
        return false;
    }

    /** Deja la lista vacía. */
    public void vaciar() {
        // Al quitar la cabeza, Java (Garbage Collector) destruye el tren automáticamente
        cabeza = null;
        cantidad = 0;
    }

    // =====================================================================
    // ETAPA 7. Representación en texto
    // =====================================================================

    /**
     * Formato: "[1, 2, 3]". Lista vacía: "[]".
     * Un elemento null se imprime como "null".
     */
    @Override
    public String toString() {
        // Usamos StringBuilder para concatenar texto de forma eficiente
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        
        // Recorremos la lista añadiendo comas solo si hay un siguiente vagón
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        
        return sb.append("]").toString();
    }

    // =====================================================================
    // ETAPA 8. Retos
    // =====================================================================

    /**
     * Invierte la lista EN SITIO: no cree nodos nuevos, solo cambie
     * las referencias 'siguiente' de los nodos existentes.
     */
    // =====================================================================
    // ETAPA 8. Retos
    // =====================================================================

    /**
     * Invierte la lista EN SITIO: no cree nodos nuevos, solo cambie
     * las referencias 'siguiente' de los nodos existentes.
     */
    public void invertir() {
        // Usamos tres punteros para invertir la dirección de los enlaces uno por uno
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        Nodo<T> siguiente = null;

        while (actual != null) {
            siguiente = actual.getSiguiente();
            actual.setSiguiente(anterior);
            anterior = actual;
            actual = siguiente;
        }
        cabeza = anterior;
    }

    /**
     * Retorna el elemento del medio. Si el tamaño es par, el segundo de los
     * dos centrales (ej.: [1,2,3,4] -> 3). Pista: dos punteros, uno avanza
     * de a 1 y otro de a 2.
     *
     * @throws NoSuchElementException si la lista está vacía
     */
    public T obtenerMedio() {
        if (estaVacia()) throw new NoSuchElementException("Lista vacía");

        // El puntero rápido avanza el doble; cuando llegue al final, el lento estará a la mitad
        Nodo<T> lento = cabeza;
        Nodo<T> rapido = cabeza;

        while (rapido != null && rapido.getSiguiente() != null) {
            lento = lento.getSiguiente();
            rapido = rapido.getSiguiente().getSiguiente();
        }
        return lento.getDato();
    }

    /**
     * Elimina los elementos repetidos dejando solo la primera aparición
     * de cada uno, conservando el orden. Ej.: [1,2,1,3,2] -> [1,2,3]
     */
    public void eliminarDuplicados() {
        Nodo<T> actual = cabeza;

        // Comparamos cada nodo con el resto del tren en adelante para borrar las copias
        while (actual != null) {
            Nodo<T> corredor = actual;
            while (corredor.getSiguiente() != null) {
                if (java.util.Objects.equals(actual.getDato(), corredor.getSiguiente().getDato())) {
                    corredor.setSiguiente(corredor.getSiguiente().getSiguiente());
                    cantidad--;
                } else {
                    corredor = corredor.getSiguiente();
                }
            }
            actual = actual.getSiguiente();
        }
    }
}
