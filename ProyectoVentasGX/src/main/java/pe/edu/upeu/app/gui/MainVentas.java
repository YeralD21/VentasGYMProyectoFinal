/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pe.edu.upeu.app.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import pe.com.syscenterlife.autocomp.AutoCompleteTextField;
import pe.com.syscenterlife.autocomp.ModeloDataAutocomplet;
import pe.com.syscenterlife.jtablecomp.ButtonsEditor;
import pe.com.syscenterlife.jtablecomp.ButtonsPanel;
import pe.com.syscenterlife.jtablecomp.ButtonsRenderer;
import pe.edu.upeu.app.dao.CarritoDAO;
import pe.edu.upeu.app.dao.CarritoDaoI;
import pe.edu.upeu.app.dao.ClienteDAO;
import pe.edu.upeu.app.dao.ClienteDaoI;
import pe.edu.upeu.app.dao.ProductoDAO;
import pe.edu.upeu.app.dao.ProductoDaoI;
import pe.edu.upeu.app.dao.VentaDAO;
import pe.edu.upeu.app.dao.VentaDaoI;
import pe.edu.upeu.app.modelo.CarritoTO;
import pe.edu.upeu.app.modelo.ClienteTO;
import pe.edu.upeu.app.modelo.VentaDetalleTO;
import pe.edu.upeu.app.modelo.VentaTO;

/**
 *
 * @author ACER ASPIRE
 */
public class MainVentas extends javax.swing.JPanel {

    /**
     * Creates new form MainVentas
     */
    ClienteDaoI daoC;
    CarritoDaoI daoCA;
    ProductoDaoI daoP;
    VentaDaoI daoV;
    List<ModeloDataAutocomplet> items;
    List<ModeloDataAutocomplet> itemsP;

    public MainVentas() {
        initComponents();
        /*ESTO HACE QUE SE LISTE*/
        //ID,NAME, OTHER
        buscarCliente();
        txtAutoCompDNI.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
                        && AutoCompleteTextField.dataGetReturnet != null) {
                    txtNombreCliente.setText(AutoCompleteTextField.dataGetReturnet.getNombreDysplay());
                    listarCarrito(txtAutoCompDNI.getText());
                    /*if (ModeloDataAutocomplet.TIPE_DISPLAY.equals("ID") && txtAutoCompDNI.getText().equals(AutoCompleteTextField.dataGetReturnet.getIdx())) {
                        txtNombreCliente.setText(AutoCompleteTextField.dataGetReturnet.getNombreDysplay());
                    } else if (ModeloDataAutocomplet.TIPE_DISPLAY.equals("NAME")
                            && txtAutoCompDNI.getText().equals(AutoCompleteTextField.dataGetReturnet.getNombreDysplay())) {
                        txtNombreCliente.setText(AutoCompleteTextField.dataGetReturnet.getIdx());
                    } else if (ModeloDataAutocomplet.TIPE_DISPLAY.equals("OTHER")
                            && txtAutoCompDNI.getText().equals(AutoCompleteTextField.dataGetReturnet.getOtherData())) {
                        System.out.println("Valor:" + txtAutoCompDNI.getText());
                        System.out.println("Valor:" + AutoCompleteTextField.dataGetReturnet.getIdx() + "\tContenido:"
                                + AutoCompleteTextField.dataGetReturnet.getNombreDysplay());
                        txtNombreCliente.setText(AutoCompleteTextField.dataGetReturnet.getIdx());
                    } else {
                        System.out.println("Valor:" + txtAutoCompDNI.getText());
                        txtNombreCliente.setText("");
                    }*/
                }
            }
        });
        buscarProducto();
        txtProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
                        && AutoCompleteTextField.dataGetReturnet != null) {
                    txtCodigo.setText(AutoCompleteTextField.dataGetReturnet.getNombreDysplay());
                    String[] dataX = AutoCompleteTextField.dataGetReturnet.getOtherData().split(":");
                    txtPU.setText(dataX[0]);
                    txtStock.setText(dataX[1]);
                }
            }
        });
        txtCantidad.addKeyListener(new KeyAdapter() {
            double pu = 0, cant = 0;

            @Override
            public void keyReleased(KeyEvent e) {

                pu = Double.parseDouble(String.valueOf(
                        txtPU.getText().equals("") ? "0" : txtPU.getText()));
                cant = Double.parseDouble(String.valueOf(
                        txtCantidad.getText().equals("") ? "0" : txtCantidad.getText()));
                txtPrecioTotal.setText(String.valueOf(pu * cant));
            }

        });
    }
    DefaultTableModel modelo;

    public List<CarritoTO> listarCarrito(String dni) {
        daoCA = new CarritoDAO();
        List<CarritoTO> listarClientes = daoCA.lista(dni);
        jTable1.setAutoCreateRowSorter(true);
        modelo = (DefaultTableModel) jTable1.getModel();
        ButtonsPanel.metaDataButtons = new String[][]{{"", "del-icon.png"}};
        jTable1.setRowHeight(40);
        TableColumn column = jTable1.getColumnModel().getColumn(8);
        column.setCellRenderer(new ButtonsRenderer());
        ButtonsEditor be = new ButtonsEditor(jTable1);
        column.setCellEditor(be);
        modelo.setNumRows(0);
        Object[] ob = new Object[9];
        double impoTotal = 0, igv = 0;
        for (int i = 0; i < listarClientes.size(); i++) {
            int x = -1;
            ob[++x] = listarClientes.get(i).getIdCarrito();
            ob[++x] = listarClientes.get(i).getDniruc();
            ob[++x] = listarClientes.get(i).getIdProducto();
            ob[++x] = listarClientes.get(i).getNombreProducto();
            ob[++x] = listarClientes.get(i).getCantidad();
            ob[++x] = listarClientes.get(i).getPunitario();
            ob[++x] = listarClientes.get(i).getPtotal();
            ob[++x] = listarClientes.get(i).getEstado();
            ob[++x] = "";
            impoTotal += Double.parseDouble(String.valueOf(listarClientes.get(i).getPtotal()));
            modelo.addRow(ob);
        }
        JButton btnDel = be.getCellEditorValue().buttons.get(0);
        btnDel.addActionListener((ActionEvent e) -> {
            System.out.println("VERRRRRR:");
            int row
                    = jTable1.convertRowIndexToModel(jTable1.getEditingRow());
            Object o = jTable1.getModel().getValueAt(row, 0);
            daoCA = new CarritoDAO();
            try {
                daoCA.delete(Integer.parseInt(String.valueOf(o)));
                listarCarrito(dni);
            } catch (Exception ex) {
                System.err.println("Error:" + ex.getMessage());
            }
            System.out.println("AAAA:" + String.valueOf(o));
            JOptionPane.showMessageDialog(this, "Has Eliminado ID: " + o);
        });
        jTable1.setModel(modelo);

        double totalDscTop = impoTotal * 0.20;
        double dt = impoTotal - totalDscTop; 
        
        txtImporteTotal.setText(String.valueOf(dt));
        /*txtImporteTotal.setText(String.valueOf(impoTotal));*/
        
        double pv = impoTotal / 1.18;
        txtPrecioB.setText(String.valueOf(Math.round(pv * 100.0) / 100.0));
        txtIgv.setText(String.valueOf(Math.round((pv * 0.18) * 100.0) / 100.0));
        
        
        txtDescuentoTop.setText(String.valueOf(Math.round(pv * 0.10) *100.0 / 100.0));
        return listarClientes;
    }

    public void buscarCliente() {
        daoC = new ClienteDAO();
        items = daoC.listAutoComplet("");
        AutoCompleteTextField.setupAutoComplete(txtAutoCompDNI, items, "ID");
    }

    public void buscarProducto() {
        daoP = new ProductoDAO();
        itemsP = daoP.listAutoComplet("");
        AutoCompleteTextField.setupAutoComplete(txtProducto, itemsP, "ID");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        txtAutoCompDNI = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPU = new javax.swing.JTextField();
        txtPrecioTotal = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtPrecioB = new javax.swing.JTextField();
        txtDescuentoTop = new javax.swing.JTextField();
        txtIgv = new javax.swing.JTextField();
        txtImporteTotal = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.MatteBorder(new javax.swing.ImageIcon(getClass().getResource("/fondoVentas1.png")))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel3.setText("Nombre/Razon Social ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 160, 20));

        jLabel2.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel2.setText("DNI/RUC cliente");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 160, 20));
        jPanel1.add(txtNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 150, -1));
        jPanel1.add(txtAutoCompDNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 150, -1));

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(204, 204, 0));
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 80, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 950, 90));

        jPanel2.setBackground(new java.awt.Color(204, 204, 0));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel4.setText("Codigo");

        jLabel5.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel5.setText("Producto");

        jLabel6.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel6.setText("P.Total");

        jLabel7.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel7.setText("Stock");

        jLabel8.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel8.setText("Cantidad");

        jLabel9.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel9.setText("P.Unitario");

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(204, 204, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data-add-icon.png"))); // NOI18N
        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtPU, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel9)
                        .addComponent(jLabel6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 950, 80));

        jPanel3.setBackground(new java.awt.Color(255, 204, 51));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setForeground(new java.awt.Color(255, 204, 102));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Dni/Ruc", "Id Producto", "Producto", "Cantidad", "P.Unit S/", "P.Total S/", "Estado", "Opc"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 950, 260));

        jPanel4.setBackground(new java.awt.Color(204, 204, 0));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel10.setText("IGV");

        jLabel11.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel11.setText("Precio Venta");

        jLabel12.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel12.setText("P.Total S/");

        jLabel13.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 3, 18)); // NOI18N
        jLabel13.setText("Descuento_Top");

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Alegreya Sans ExtraBold", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(204, 204, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/shop-cart-add-icon.png"))); // NOI18N
        jButton3.setText("R.Venta");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecioB, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txtIgv, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(txtDescuentoTop, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(txtImporteTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel13)
                        .addGap(55, 55, 55)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(259, 259, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescuentoTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImporteTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIgv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 516, 950, 80));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blackfriday.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 600));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        registarVenta();
    }//GEN-LAST:event_jButton3ActionPerformed
    public void limpiarCarrito() {
        daoCA = new CarritoDAO();
        daoCA.deleteCarAll(txtAutoCompDNI.getText());
        listarCarrito(txtAutoCompDNI.getText());
    }

    public void registarVenta() {
        daoV = new VentaDAO();

        List<CarritoTO> lista = listarCarrito(txtAutoCompDNI.getText());
        VentaTO tov = new VentaTO();
        tov.setDniruc(txtAutoCompDNI.getText());
        tov.setIgv(Double.parseDouble(txtIgv.getText()));
        tov.setDescuento_top(Double.parseDouble(txtDescuentoTop.getText()));
        tov.setPrecioBase(Double.parseDouble(txtPrecioB.getText()));
        tov.setPrecioTotal(Double.parseDouble(txtImporteTotal.getText()));
        int idx = daoV.createVenta(tov);
        if (idx != 0) {
            for (CarritoTO carritoTO : lista) {
                daoV = new VentaDAO();
                VentaDetalleTO vd = new VentaDetalleTO();
                vd.setIdVenta(idx);
                vd.setIdProducto(carritoTO.getIdProducto());
                vd.setCantidad(carritoTO.getCantidad());
                vd.setPu(carritoTO.getPunitario());
                vd.setSubTotal(carritoTO.getPtotal());
                vd.setDescuento(0);
                daoV.createVentaDetalle(vd);
            }
        }
        limpiarCarrito();
        /*runReport1(idx);*/

    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FormCliente pvc = new FormCliente();
        JOptionPane.showOptionDialog(null, pvc,
                "Registrar Cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, new Object[]{}, null);
// System.out.println(x);
        if (pvc.capturarCliente() != null) {
            buscarCliente();
            ClienteTO tt = (ClienteTO) pvc.capturarCliente();
            System.out.println("dniruc:" + tt.getDniruc());
            txtNombreCliente.setText(tt.getNombres());
            txtAutoCompDNI.setText(tt.getDniruc());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        daoCA = new CarritoDAO();
        CarritoTO to = new CarritoTO();
        to.setDniruc(txtAutoCompDNI.getText());
        to.setIdProducto(Integer.parseInt(txtCodigo.getText()));
        to.setNombreProducto(txtProducto.getText());
        to.setCantidad(Double.parseDouble(txtCantidad.getText()));
        to.setPunitario(Double.parseDouble(txtPU.getText()));
        to.setPtotal(Double.parseDouble(txtPrecioTotal.getText()));
        to.setEstado(0);
        daoCA.crear(to);
        listarCarrito(txtAutoCompDNI.getText());
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtAutoCompDNI;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescuentoTop;
    private javax.swing.JTextField txtIgv;
    private javax.swing.JTextField txtImporteTotal;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtPU;
    private javax.swing.JTextField txtPrecioB;
    private javax.swing.JTextField txtPrecioTotal;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
