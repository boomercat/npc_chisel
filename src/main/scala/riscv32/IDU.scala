package riscv32

import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import ctrlwire._

class IDUIO extends Bundle {
  val instruction = Input(UInt(32.W))
  val imm         = Output(UInt(32.W))
  val regAdd      = new BundleReg()
  val bundleCtrl  = new BundleCtrl()
}

class IDU extends Module {
  val io        = IO(new IDUIO())
  val imm       = WireDefault(0.U(32.W))
  val rs1_valid = WireDefault(0.U(1.W))
  val rs2_valid = WireDefault(0.U(1.W))
  val rd_valid  = WireDefault(0.U(1.W))
  val imm_type  = WireDefault(0.U(3.W))

  io.regAdd.rs1 := Mux(rs1_valid === 1.U, io.instruction(19, 15), 0.U)
  io.regAdd.rs2 := Mux(rs2_valid === 1.U, io.instruction(24, 20), 0.U)
  io.regAdd.rd  := Mux(rd_valid  === 1.U, io.instruction(11, 7), 0.U)

  val imm_i = Cat(Fill(20, io.instruction(31)), io.instruction(31, 20))
  val imm_s = Cat(Fill(20, io.instruction(31)), io.instruction(31, 25), io.instruction(11, 7))
  val imm_b = Cat(Fill(20, io.instruction(31)), io.instruction(7), io.instruction(30, 25), io.instruction(11, 8), 0.U(1.W))
  val imm_u = Cat(io.instruction(31, 12), Fill(12, 0.U))
  val imm_j = Cat(Fill(12, io.instruction(31)), io.instruction(31), io.instruction(19, 12), io.instruction(20), io.instruction(30, 21), Fill(1, 0.U))

  // 定义解码表
  val default = List(0.U(3.W), 0.U(5.W), 0.U(1.W), 0.U(1.W), 0.U(1.W), 0.U(1.W))
  val decodeTable = Seq(
    // {imm_type(3), alu_ctrl(5), reg_write(1), rs1_valid, rs2_valid, rd_valid}
    BitPat("b????????????????????_?????_0010111") -> List(3.U(3.W), 1.U(5.W), 1.U(1.W), 1.U(1.W), 1.U(1.W), 1.U(1.W))
    // 在这里添加更多解码表项
  ).toArray

  // 根据指令解码
  val controlValues = ListLookup(io.instruction, default, decodeTable)

  imm_type := controlValues(0)

  when(imm_type === 1.U) {
    imm := imm_s
  }.elsewhen(imm_type === 2.U) {
    imm := imm_b
  }.elsewhen(imm_type === 3.U) {
    imm := imm_u
  }.elsewhen(imm_type === 4.U) {
    imm := imm_j
  }.otherwise {
    imm := imm_i
  }

  io.bundleCtrl.alu_ctrl_op    := controlValues(1)
  io.bundleCtrl.reg_ctrl_write := controlValues(2)
  rs1_valid                    := controlValues(3)
  rs2_valid                    := controlValues(4)
  rd_valid                     := controlValues(5)

  io.imm := imm
}

