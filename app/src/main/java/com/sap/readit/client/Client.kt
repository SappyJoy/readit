package com.sap.readit.client

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.sap.readit.utils.Unpacker
import java.io.*
import java.net.Socket
import java.net.URL
import java.nio.charset.Charset


class Client {
    private var clientSocket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null
    fun startConnection(ip: String?, port: Int) {
        clientSocket = Socket(ip, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))

        // Для тестирования из консоли
//        val sc = Scanner(System.`in`)
//
//        while (sc.hasNextLine()) {
//            val response = sendMessage(sc.nextLine()) ?: break
//            val txtFile: File?
//            try {
//                txtFile = Unpacker.unpackArchive(URL(response), File("res"))
//            } catch (e: IOException) {
//                System.err.println(e.message)
//                continue
//            }
//            if (txtFile == null) {
//                System.err.println("Файл не найден")
//                continue
//            }
//            val br = BufferedReader(InputStreamReader(FileInputStream(txtFile),
//                Charset.forName("windows-1251")))
//
//            var i = 0
//            val lines = br.readLines()
//            val count = lines.count()
//            for (line in lines) {
//                i++
//                if ((i in 1..11) || (i in count-9 until count))
//                    continue
//                println(line)
//            }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun nextText(msg: String?): String? {
        val response = sendMessage(msg)
        val txtFile: File?
        try {
            Environment.getExternalStorageState()
            txtFile = Unpacker.unpackArchive(URL(response), File("res"))
        } catch (e: IOException) {
            System.err.println(e.message)
            return nextText(msg)
        }
        if (txtFile == null) {
            System.err.println("Файл не найден")
            return nextText(msg)
        }
        val br = BufferedReader(InputStreamReader(FileInputStream(txtFile),
            Charset.forName("windows-1251")))

        val sb = StringBuffer()
        var i = 0
        val lines = br.readLines()
        val count = lines.count()
        for (line in lines) {
            i++
            if ((i in 1..11) || (i in count-9 until count))
                continue
//            println(line)
            sb.append(line + "\n")
        }
        return sb.toString()
    }

    fun sendMessage(msg: String?): String? {
        out!!.println(msg)
        val response = `in`!!.readLine().replace("$", "\n")
        return response
    }

    fun stopConnection() {
        `in`!!.close()
        out!!.close()
        clientSocket!!.close()
    }
}