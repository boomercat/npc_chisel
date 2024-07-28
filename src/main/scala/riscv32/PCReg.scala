package riscv32

import chisel3._
import chisel3.util._

class PCRegIO extends Bundle{
    // val resultBranch = Input(Bool())
    // // val pcJump       = Input(Bool())
    // val resultpc     = Input(UInt(32.W))
    val pc           = Output(UInt(32.W))
}

class PCReg extends Module {
  val io = IO(new PCRegIO())

  // 使用无符号数表示初始化值
  val regPC = RegInit("h80000000".U(32.W))
  regPC := regPC + 4.U

  io.pc := regPC
}

  

