package ar.edu.utn.frsf.isi.dam;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DecimalFormat f = new DecimalFormat("##.00");

    ElementoMenu[] listaBebidas;
    ElementoMenu[] listaPlatos;
    ElementoMenu[] listaPostre;

    List<ElementoMenu> pedidoArmado = new ArrayList<ElementoMenu>();
    ;
    String item;

    ListView listView;

    ToggleButton toggle;

    Button agregar;
    Button botonReiniciar;
    Button botonConfirmar;

    Boolean pedidoConfirmado = false;
    ElementoMenu elementoMenu = null;
    TextView listadoAPedir;


    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listViewElementoMenu);
        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        agregar = (Button) findViewById(R.id.botonAgregar);
        botonReiniciar = (Button) findViewById(R.id.botonReiniciar);
        botonConfirmar = (Button) findViewById(R.id.botonConfirmar);
        listadoAPedir = (TextView) findViewById(R.id.listadoAPedir);

        iniciarListas();

        final ArrayList array = new ArrayList();

        final ArrayAdapter adapter = new ArrayAdapter<ArrayList>(this, android.R.layout.simple_list_item_single_choice);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    toggle.setText("Apreto");
                } else {
                    toggle.setText("No apreto");
                }
            }
        });


        botonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Debemos borrar el listado a pedir y actualizar

                pedidoArmado.clear();
                pedidoConfirmado=false;
                actualizarListaPedidos();

                Toast.makeText(getBaseContext(), "Se ha reiniciado su pedido ", Toast.LENGTH_SHORT).show();


            }
        });

        botonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedidoConfirmado=true;
                actualizarListaPedidos();

                Toast.makeText(getBaseContext(), "Se ha confirmado su pedido ", Toast.LENGTH_SHORT).show();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pedidoConfirmado) {
                    Toast.makeText(getBaseContext(), "No se pueden agregar más elementos", Toast.LENGTH_SHORT).show();
                }else{
                            /* ElementoMenu elementoMenu = (ElementoMenu) listView.getSelectedItem();
                             Toast.makeText(getBaseContext(), elementoMenu.toString(), Toast.LENGTH_LONG).show();*/

                    //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                    // item Es el texto del elemento seleccionado.
                    // Debemos agregarlo a una lista o al textview

                    if(elementoMenu==null){
                        Toast.makeText(getBaseContext(), "Se debe seleccionar un elemento", Toast.LENGTH_SHORT).show();
                    }else {

                        // Mostramos todos los datos guardados
                        Toast.makeText(getBaseContext(), "Ha agregado " + elementoMenu.getNombre(), Toast.LENGTH_SHORT).show();

                        pedidoArmado.add(elementoMenu);

                        actualizarListaPedidos();
                        //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position,
                                    long id) {



                // Elemento pedido
                elementoMenu = (ElementoMenu) listView.getItemAtPosition(position);


            }
        });

        listadoAPedir.setMovementMethod(new ScrollingMovementMethod());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radioButtonPlato) {

                    mostrarLista(array, adapter, listaPlatos, "Usted ha seleccionado platos");

                } else if (checkedId == R.id.radioButtonPostre) {

                    mostrarLista(array, adapter, listaPostre, "Usted ha seleccionado postres");

                } else if (checkedId == R.id.radioButtonBebida) {

                    mostrarLista(array, adapter, listaBebidas, "Usted ha seleccionado bebidas");

                } else {
                    Toast.makeText(getApplicationContext(), "choice: Vibration",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void mostrarLista(ArrayList array, ArrayAdapter adapter, ElementoMenu[] lista, String texto) {


        listView.clearChoices();
        listView.setItemChecked(-1, true);
        elementoMenu = null;
        //listView.setAdapter(null);

        // Limpiamos el array y le cargamos la lista con los elementos a mostrar
        array.clear();
        array.addAll(Arrays.asList(lista));

        // Limpiamos el adapter
        adapter.clear();
        adapter.addAll(array);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        // listView.setOnItemClickListener(new OnItemClickListenerListViewItem());

        // Mostramos un mensaje indicando el tipo de comida mostrada en la lista
        Toast.makeText(getApplicationContext(), texto,
                Toast.LENGTH_SHORT).show();
    }

    class ElementoMenu {
        private Integer id;
        private String nombre;
        private Double precio;

        public ElementoMenu() {
        }

        public ElementoMenu(Integer i, String n, Double p) {
            this.setId(i);
            this.setNombre(n);
            this.setPrecio(p);
        }

        public ElementoMenu(Integer i, String n) {
            this(i, n, 0.0);
            Random r = new Random();
            this.precio = (r.nextInt(3) + 1) * ((r.nextDouble() * 100));
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        @Override
        public String toString() {
            return this.nombre + "( " + f.format(this.precio) + ")";
        }
    }

    private void actualizarListaPedidos() {
        // Llega una lista, y la debemos mostrar en el textview
        listadoAPedir.setText("");

        StringBuilder builder = new StringBuilder();
        Double total=0.0;

        for (int i = 0; i < pedidoArmado.size(); i++) {

            Integer elemento = i+1;
            builder.append("Elemento " + elemento + " : " + pedidoArmado.get(i).toString() + "\n");
            total+= pedidoArmado.get(i).getPrecio();
        }

        if(pedidoConfirmado){
            builder.append("Total: $"+f.format(total));
        }
        listadoAPedir.setText(builder.toString());


    }

    private void iniciarListas() {

        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0] = new ElementoMenu(1, "Coca");
        listaBebidas[1] = new ElementoMenu(4, "Jugo");
        listaBebidas[2] = new ElementoMenu(6, "Agua");
        listaBebidas[3] = new ElementoMenu(8, "Soda");
        listaBebidas[4] = new ElementoMenu(9, "Fernet");
        listaBebidas[5] = new ElementoMenu(10, "Vino");
        listaBebidas[6] = new ElementoMenu(11, "Cerveza");
        // inicia lista de platos
        listaPlatos = new ElementoMenu[14];
        listaPlatos[0] = new ElementoMenu(1, "Ravioles");
        listaPlatos[1] = new ElementoMenu(2, "Gnocchi");
        listaPlatos[2] = new ElementoMenu(3, "Tallarines");
        listaPlatos[3] = new ElementoMenu(4, "Lomo");
        listaPlatos[4] = new ElementoMenu(5, "Entrecot");
        listaPlatos[5] = new ElementoMenu(6, "Pollo");
        listaPlatos[6] = new ElementoMenu(7, "Pechuga");
        listaPlatos[7] = new ElementoMenu(8, "Pizza");
        listaPlatos[8] = new ElementoMenu(9, "Empanadas");
        listaPlatos[9] = new ElementoMenu(10, "Milanesas");
        listaPlatos[10] = new ElementoMenu(11, "Picada 1");
        listaPlatos[11] = new ElementoMenu(12, "Picada 2");
        listaPlatos[12] = new ElementoMenu(13, "Hamburguesa");
        listaPlatos[13] = new ElementoMenu(14, "Calamares");
        // inicia lista de postres
        listaPostre = new ElementoMenu[15];
        listaPostre[0] = new ElementoMenu(1, "Helado");
        listaPostre[1] = new ElementoMenu(2, "Ensalada de Frutas");
        listaPostre[2] = new ElementoMenu(3, "Macedonia");
        listaPostre[3] = new ElementoMenu(4, "Brownie");
        listaPostre[4] = new ElementoMenu(5, "Cheescake");
        listaPostre[5] = new ElementoMenu(6, "Tiramisu");
        listaPostre[6] = new ElementoMenu(7, "Mousse");
        listaPostre[7] = new ElementoMenu(8, "Fondue");
        listaPostre[8] = new ElementoMenu(9, "Profiterol");
        listaPostre[9] = new ElementoMenu(10, "Selva Negra");
        listaPostre[10] = new ElementoMenu(11, "Lemon Pie");
        listaPostre[11] = new ElementoMenu(12, "KitKat");
        listaPostre[12] = new ElementoMenu(13, "IceCreamSandwich");
        listaPostre[13] = new ElementoMenu(14, "Frozen Yougurth");
        listaPostre[14] = new ElementoMenu(15, "Queso y Batata");

    }
}
