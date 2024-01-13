package devandroid.esther.bebergua_lembrete

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import devandroid.esther.bebergua_lembrete.databinding.ActivityMainBinding
import devandroid.esther.bebergua_lembrete.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var btn_calcular: Button
    private lateinit var txt_resultado_ml: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var btn_lembrate: Button
    private lateinit var btn_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var calcularIngestaoDiaria: CalcularIngestaoDiaria
    private var resultadoML = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //supportActionBar!!.hide()
       // iniciarComponentes()
        calcularIngestaoDiaria= CalcularIngestaoDiaria()

        binding.btnCalcular.setOnClickListener{

            if(binding.editPeso.text.toString().isEmpty()){
                Toast.makeText(this, R.string.toast_informe_peso, Toast.LENGTH_SHORT).show()
            }else if(binding.editIdade.text.toString().isEmpty()){
                Toast.makeText(this, R.string.toast_informe_idade, Toast.LENGTH_SHORT).show()
            }else{
                val peso = binding.editPeso.text.toString().toDouble()
                val idade = binding.editIdade.text.toString().toInt()

                calcularIngestaoDiaria.calcularTotalML(peso, idade)
                resultadoML = calcularIngestaoDiaria.resultadoML()
                val formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                binding.txtResultadoMl.text = formatar.format(resultadoML)+ " " + "ml"
            }
        }

        binding.icRedefinir.setOnClickListener{
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.dialogo_titulo)
                .setMessage(R.string.dialogo_desc)
                .setPositiveButton("Ok", { dialogInterface, i ->
                    binding.editPeso.setText("")
                    binding.editIdade.setText("")
                    binding.txtResultadoMl.text = ""
                })
            alertDialog.setNegativeButton( "Cancelar", { dialogoInterface, i ->

            })
            val dialog = alertDialog.create()
            dialog.show()
        }

        binding.btnDefinirLembrete.setOnClickListener{

            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog = TimePickerDialog(this, { timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                binding.txtHora.text= String.format("%02d", hourOfDay)
                binding.txtMinutos.text= String.format("%02d", minutes)
            },horaAtual, minutosAtuais, true)
            timePickerDialog.show()
        }

        binding.btnAlarme.setOnClickListener {

            val txt_hora = binding.txtHora
            val txt_minutos = binding.txtMinutos

            if (!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES, txt_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.alarme_mensagem))
                startActivity(intent)

                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
            }
        }
    }
    /*private fun iniciarComponentes(){
      edit_peso = findViewById(R.id.edit_peso)
        edit_idade = findViewById(R.id.edit_idade)
        btn_calcular = findViewById(R.id.btn_calcular)
        txt_resultado_ml = findViewById(R.id.txt_resultado_ml)
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
        btn_lembrate = findViewById(R.id.btn_definir_lembrete)
        btn_alarme = findViewById(R.id.btn_alarme)
        txt_hora = findViewById(R.id.txt_hora)
        txt_minutos = findViewById(R.id.txt_minutos)
    }*/
}