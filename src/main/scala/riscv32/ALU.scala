package riscv32

import chisel3._
import chisel3.util._

import config.Configs._
import ctrlwire._
import ctrlwire.Alu_ctrl_wire._

class AluIO extends Bundle {
  val bundleAluControl = new BundleAlu()
  val dataRead1        = Input(UInt(32.W))
  val dataRead2        = Input(UInt(32.W))
  val imm              = Input(UInt(32.W))
  val pc               = Input(UInt(32.W))
  val alu_result       = Output(UInt(32.W))
  // val pc_jump          = Output(UInt(32.W))
}

class ALU extends Module {
  val io = IO(new AluIO()) // 确保 IO 正确初始化

  val alu_result = WireDefault(0.U(32.W)) // 确保位宽一致
  val src1 = WireDefault(0.U(32.W)) // 确保位宽一致
  val src2 = WireDefault(0.U(32.W)) // 确保位宽一致

  // 将 src1 和 src2 初始化为 dataRead1 和 dataRead2
  src1 := io.dataRead1
  src2 := io.dataRead2
  switch(io.bundleAluControl.alu_ctrl_op) {
    is(alu_nop) { alu_result := 0.U }
    is(alu_add) { alu_result := src1 + src2 }
    // 添加更多操作符
  }

  io.alu_result := alu_result
}
