# VentasGYMProyectoFinal
FABRIZIO (usuario, contraseña)
String uname = txtuser.getText();
        String pword = txtclave.getText();
        if (uname.equals("") || pword.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Ocurrio un error", "UPS¡", 1);

        } else {
            try {
                connection = Conn.connectSQLite();
                ps = connection.prepareStatement("SELECT * FROM usuario WHERE user=? and clave=?");

                ps.setString(1, uname);
                ps.setString(2, pword);
                rs = ps.executeQuery();

                if (rs.next()) {
                    GUIMain guiMain = new GUIMain();
                    guiMain.setVisible(true);
                    this.dispose();
                }

            } catch (Exception ex) {
                System.out.println("" + ex);
            }
        }
FABRIZIO (NUEVA BASE DE DATOS)
    -Cliente, Usuario, Producto, Categoria, Marca, Venta, VentaDetalle, Carrito.

FABRIZIO JFRAME - JPANEL (DISEÑOS LOGIN Y MAIN VENTA)