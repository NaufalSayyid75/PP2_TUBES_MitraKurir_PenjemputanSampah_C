import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PenjemputanSampahKurir extends JFrame {
    // Komponen GUI
    private JTextField txtNama, txtAlamat, txtJenis, txtTanggal, txtStatus;
    private JTextArea txtDeskripsi;
    private JButton btnTambah, btnUbah, btnHapus, btnLihat;
    private JTable tblData;
    private DefaultTableModel model;

    // File untuk menyimpan data
    private static final String FILE_NAME = "penjemputan_sampah.txt";

    public PenjemputanSampahKurir() {
        setTitle("Penjemputan Sampah oleh Kurir");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Inisialisasi komponen
        JLabel lblNama = new JLabel("Nama Pelanggan:");
        JLabel lblAlamat = new JLabel("Alamat:");
        JLabel lblJenis = new JLabel("Jenis Sampah:");
        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        JLabel lblTanggal = new JLabel("Tanggal Penjemputan (YYYY-MM-DD):");
        JLabel lblStatus = new JLabel("Status Penjemputan:");

        txtNama = new JTextField();
        txtAlamat = new JTextField();
        txtJenis = new JTextField();
        txtDeskripsi = new JTextArea();
        txtTanggal = new JTextField();
        txtStatus = new JTextField();

        btnTambah = new JButton("Tambah Data");
        btnUbah = new JButton("Ubah Data");
        btnHapus = new JButton("Hapus Data");
        btnLihat = new JButton("Lihat Data");

        model = new DefaultTableModel(new String[]{"ID", "Nama", "Alamat", "Jenis", "Deskripsi", "Tanggal", "Status"}, 0);
        tblData = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblData);

        // Posisi komponen
        lblNama.setBounds(30, 30, 150, 25);
        txtNama.setBounds(200, 30, 200, 25);
        lblAlamat.setBounds(30, 70, 150, 25);
        txtAlamat.setBounds(200, 70, 200, 25);
        lblJenis.setBounds(30, 110, 150, 25);
        txtJenis.setBounds(200, 110, 200, 25);
        lblDeskripsi.setBounds(30, 150, 150, 25);
        txtDeskripsi.setBounds(200, 150, 200, 60);
        lblTanggal.setBounds(30, 220, 200, 25);
        txtTanggal.setBounds(250, 220, 150, 25);
        lblStatus.setBounds(30, 260, 200, 25);
        txtStatus.setBounds(250, 260, 150, 25);

        btnTambah.setBounds(30, 310, 120, 25);
        btnUbah.setBounds(160, 310, 120, 25);
        btnHapus.setBounds(290, 310, 120, 25);
        
        scrollPane.setBounds(30, 360, 820, 200);

        add(lblNama);
        add(txtNama);
        add(lblAlamat);
        add(txtAlamat);
        add(lblJenis);
        add(txtJenis);
        add(lblDeskripsi);
        add(txtDeskripsi);
        add(lblTanggal);
        add(txtTanggal);
        add(lblStatus);
        add(txtStatus);
        add(btnTambah);
        add(btnUbah);
        add(btnHapus);
        add(scrollPane);

        // Event tombol
        btnTambah.addActionListener(e -> tambahData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        

        // Muat data awal
        lihatData();
    }

    private void tambahData() {
        String nama = txtNama.getText();
        String alamat = txtAlamat.getText();
        String jenis = txtJenis.getText();
        String deskripsi = txtDeskripsi.getText();
        String tanggal = txtTanggal.getText();
        String status = txtStatus.getText();

        if (nama.isEmpty() || alamat.isEmpty() || jenis.isEmpty() || deskripsi.isEmpty() || tanggal.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            int id = getNextId();
            writer.write(id + "," + nama + "," + alamat + "," + jenis + "," + deskripsi + "," + tanggal + "," + status);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan.");
            lihatData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
            e.printStackTrace();
        }
    }

    private void hapusData() {
        int row = tblData.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) != id) {
                    lines.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
            lihatData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data.");
            e.printStackTrace();
        }
    }

    private void lihatData() {
        model.setRowCount(0); // Bersihkan tabel
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5], data[6]});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data.");
            e.printStackTrace();
        }
    }

    private void ubahData() {
        int row = tblData.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah.");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        String nama = txtNama.getText();
        String alamat = txtAlamat.getText();
        String jenis = txtJenis.getText();
        String deskripsi = txtDeskripsi.getText();
        String tanggal = txtTanggal.getText();
        String status = txtStatus.getText();

        if (nama.isEmpty() || alamat.isEmpty() || jenis.isEmpty() || deskripsi.isEmpty() || tanggal.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data harus diisi.");
            return;
        }

        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == id) {
                    lines.add(id + "," + nama + "," + alamat + "," + jenis + "," + deskripsi + "," + tanggal + "," + status);
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();

            JOptionPane.showMessageDialog(this, "Data berhasil diubah.");
            lihatData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data.");
            e.printStackTrace();
        }
    }

    

   private int getNextId() {
        int id = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                id = Math.max(id, Integer.parseInt(data[0]) + 1);
            }
        } catch (IOException e) {
            // Tidak ada file atau error membaca
        }
        return id;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PenjemputanSampahKurir().setVisible(true));
    }

}