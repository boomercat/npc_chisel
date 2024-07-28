package riscv32

import chisel3._
import chisel3.util._

import config.Configs._
import ctrlwire._

class TopIO extends Bundle {
  val pc = Output(UInt(DATA_WIDTH.W))
  val instruction = Output(UInt(DATA_WIDTH.W))
  val bundleCtrl = new BundleCtrl()
  val rs1 = Output(UInt(ADDR_WIDTH.W))
  val rs2 = Output(UInt(DATA_WIDTH.W))
  val imm = Output(UInt(DATA_WIDTH.W))
  val alu_result = Output(UInt(DATA_WIDTH.W))
}

class Top extends Module {
  val io = IO(new TopIO)

  val pcReg = Module(new PCReg())
  val decoder = Module(new IDU())
  val register = Module(new Register())
  val alu = Module(new ALU())
  val controller = Module(new Controller())
  val ifu = Module(new IFU())

  // Connecting IFU
  ifu.io.pc := pcReg.io.pc
  io.instruction := ifu.io.instruction
  decoder.io.instruction := ifu.io.instruction

  // Connecting Register
  register.io.reg_ctrl_write := controller.io.bundleControlOut.reg_ctrl_write
  register.io.bundleReg := decoder.io.regAdd
  register.io.writedata := alu.io.alu_result
  io.rs1 := register.io.dataRead1
  io.rs2 := register.io.dataRead2

  // Connecting ALU
  alu.io.imm := decoder.io.imm
  alu.io.pc := pcReg.io.pc
  alu.io.bundleAluControl := controller.io.bundleAluctrl
  alu.io.dataRead1 := register.io.dataRead1
  alu.io.dataRead2 := register.io.dataRead2
  io.alu_result := alu.io.alu_result

  // Connecting Controller
  controller.io.bundleCtrlIn := decoder.io.bundleCtrl

  // Connecting outputs
  io.pc := pcReg.io.pc
  io.bundleCtrl := decoder.io.bundleCtrl
  io.imm := decoder.io.imm
}
