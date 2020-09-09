package idn.fahru.aplikasitodogambar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import idn.fahru.aplikasitodogambar.databinding.ActivityMainBinding
import idn.fahru.aplikasitodogambar.model.ModelData
import idn.fahru.aplikasitodogambar.recyclerview.adapter.ItemDataAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // buat variabel adapter untuk recyclerview
    private lateinit var adapterMain: ItemDataAdapter

    // TODO(Buat Binding Untuk activity_main.xml)
    private lateinit var databaseUser: DatabaseReference

    //buat variabel ValueEventListener
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO (Buat Binding Untuk activity_main.xml
        val inflater = layoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        // TODO(isi AdapterMain sebagai adapter recycleview yang ada di activity_main.xml)
        adapterMain = ItemDataAdapter()

        binding.extendedFab.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }

        // setting RecyclerView
        binding.rvMain.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMain
            setHasFixedSize(true)
        }

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //TODO(buat daftarUser berupa array kosong untuk ModelData
                val daftarUser = arrayListOf<ModelData>()

                //TODO(jika ada data di database, maka isi data tersebut ke dalam daftar user)
                if(snapshot.childrenCount > 0) {
                    for (dataUser in snapshot.children) {
                        val data = dataUser.getValue(ModelData::class.java) as ModelData
                        daftarUser.add(data)
                    }

                    //TODO(Tambahkan Daftar user ke dalamadapter main)
                    adapterMain.addData(daftarUser)
                } else {
                    adapterMain.notifyDataSetChanged()
                }
                //TODO(beritahu adapter jika ada perubahan data)
                adapterMain.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        //TODO (isi nilai dari databaseuser berisi lokasi dari realtime dataase "Users
        databaseUser = FirebaseDatabase.getInstance().reference.child("Users")
        databaseUser .addChildEventListener(valueEventListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        // ini jangan dihapus.. setiap kali kita menambahkan eventlistener
        // maka perlu dihapus dengan cara removeEventListener
        // jika penambahan terjadi di oncreate
        // maka hapusnya itu ada di onDestroy seperti kode di bawah ini
        databaseUser.removeEventListener(valueEventListener)
    }
}

private fun DatabaseReference.addChildEventListener(valueEventListener: ValueEventListener) {

}
