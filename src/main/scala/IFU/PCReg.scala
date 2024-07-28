package riscv32

import chisel3._
import chisel3.util._

class PCRegIO extends Bundle{
    val resultBranch = Input(Bool())
    val pcJump       = Input(Bool())
    val resultpc     = Input(UInt(32.W))
    val pc           = Output(UInt(32.W))
}

class PCReg extends Module {
  val io = IO(new PCRegIO())
    
    val regPC = RegInit(0x80000000.U(32.W))
    when(io.pcJump || io.resultBranch){
        regPC := io.resultpc
    }.otherwise{
        regPC := regPC + 4.U
    }

    val pc := regPC
}

  

