package riscv32
import chisel3._
import chisel3.util._
import chisel3.experimental._

class PmemBlackBox extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(Bool())
    val pc = Input(UInt(32.W))
    val instruction = Output(UInt(32.W))
  })

  // 在这里插入Verilog代码
  setInline("PmemBlackBox.v",
    s"""
       |module pc_transfer_inst(
       |    input clock,
       |    input rst,
       |    input [31:0] pc,
       |    output [31:0] instruction
       |);
       |import "DPI-C" function int unsigned pmem_read(input int unsigned raddr, int len);
       |
       |assign instruction = pmem_read(pc, 4);
       |
       |endmodule
     """.stripMargin)
}
