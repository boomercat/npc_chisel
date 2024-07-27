package ctrlwire

import chisel3._

class RegAddBundle extends  Bundle{
    val rs1 = Output(UInt(5.W))
    val rs2 = Output(UInt(5.W))
    val rd  = Output(UInt(5.W))
}