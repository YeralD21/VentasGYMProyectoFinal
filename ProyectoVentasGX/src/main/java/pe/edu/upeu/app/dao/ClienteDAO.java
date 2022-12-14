/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import pe.edu.upeu.app.dao.conx.Conn;
import pe.edu.upeu.app.modelo.ClienteTO;
import pe.edu.upeu.app.util.ErrorLogger;

/**
 *
 * @author ACER ASPIRE
 */
public class ClienteDAO implements ClienteDaoI {

    Statement stmt = null;
    Vector columnNames;
    Vector visitdata;
    Connection connection = Conn.connectSQLite();
    static PreparedStatement ps;
    static ErrorLogger log = new ErrorLogger(ClienteDAO.class.getName());
    ResultSet rs = null;

    public ClienteDAO() {
        columnNames = new Vector();
        visitdata = new Vector();
    }

    @Override
    public int create(ClienteTO d) {
        int rsId = 0;
        String sql = "INSERT INTO cliente(dniruc, nombres, plan, fecha_inicio, fecha_final, clinete_top, descuento) "
                + "VALUES(?,?,?,?,?,?,?)";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(++i, d.getDniruc());
            ps.setString(++i, d.getNombres());
            ps.setString(++i, d.getPlan());
            ps.setString(++i, d.getFecha_inicio());
            ps.setString(++i, d.getFecha_final());
            ps.setString(++i, d.getCliente_top());
            ps.setString(++i, d.getDescuento());
          
            
            rsId = ps.executeUpdate();// 0 no o 1 si commit
            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rsId = rs.getInt(1);
                }
                rs.close();
            }
        } catch (SQLException ex) {
//System.err.println("create:" + ex.toString());
            log.log(Level.SEVERE, "create", ex);
        }
        return rsId;
    }

    @Override
    public int update(ClienteTO d) {
        System.out.println("actualizar d.getCliente_top: " + d.getCliente_top());
        System.out.println("actualizar d.getDescuento: " + d.getDescuento());

        int comit = 0;
        String sql = "UPDATE cliente SET "
                + ""
                
                + "cliente_top=?, "
                + "descuento=?, "
              
                
                + "WHERE dniruc=?";
        int i = 0;
        try {
            ps = connection.prepareStatement(sql);
           
            ps.setString(++i, d.getCliente_top());
            ps.setString(++i, d.getDescuento());
            comit = ps.executeUpdate();
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "update", ex);
        }
        return comit;
    }

    @Override
    public int delete(String id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ClienteTO> listCmb(String filter) {
 
        List<ClienteTO> ls = new ArrayList();
        ls.add(new ClienteTO());
        ls.addAll(listarClientes());
        return ls;
    }

    @Override
    public List <ClienteTO> listarClientes() {
        List<ClienteTO> listarclientes = new ArrayList();
        String sql = "SELECT * FROM cliente";
        try {
            connection = new Conn().connectSQLite();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ClienteTO cli = new ClienteTO();
                cli.setDniruc(rs.getString("dniruc"));
                cli.setNombres(rs.getString("nombres"));
                cli.setPlan(rs.getString("plan"));
                cli.setFecha_inicio(rs.getString("fecha_inicio"));
                cli.setFecha_final(rs.getString("fecha_final"));
                cli.setCliente_top(rs.getString("cliente_top"));
                cli.setDescuento(rs.getString("descuento"));
           
                listarclientes.add(cli);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listarclientes;
    }

    @Override
    public ClienteTO buscarClientes(String dni) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void reportarCliente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
