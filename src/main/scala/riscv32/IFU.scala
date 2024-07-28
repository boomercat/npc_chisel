package riscv32

import chisel3._
import chisel3.util._

import config.Configs._


class IFUIO extends Bundle {
  val pc = Input(UInt(ADDR_WIDTH.W))
  val instruction = Output(UInt(ADDR_WIDTH.W))
}

class IFU extends Module {
  val io = IO(new IFUIO())

  val pmem = Module(new PmemBlackBox)

  pmem.io.clock := clock
  pmem.io.reset := reset
  pmem.io.pc := io.pc
  io.instruction := pmem.io.instruction
}

