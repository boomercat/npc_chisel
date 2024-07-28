package riscv32

import chisel3._
import chisel3.util._

import config.Configs._
import ctrlwire._

class RegisterIO extends Bundle {
  val reg_ctrl_write = Input(UInt(1.W))
  val writedata = Input(UInt(DATA_WIDTH.W))
  val dataRead1 = Output(UInt(DATA_WIDTH.W))
  val dataRead2 = Output(UInt(DATA_WIDTH.W))
  val bundleReg = Input(new BundleReg())
}

class Register extends Module {
  val io = IO(new RegisterIO())
  
  val regs = Reg(Vec(REG_NUM, UInt(DATA_WIDTH.W)))
  
  when(io.bundleReg.rs1 === 0.U) {
    io.dataRead1 := 0.U
  } .otherwise {
    io.dataRead1 := regs(io.bundleReg.rs1)
  }
  
  when(io.bundleReg.rs2 === 0.U) {
    io.dataRead2 := 0.U
  } .otherwise {
    io.dataRead2 := regs(io.bundleReg.rs2)
  }

  when(io.reg_ctrl_write === 1.U && (io.bundleReg.rd =/= 0.U)) {
    regs(io.bundleReg.rd) := io.writedata
  }
}