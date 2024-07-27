package riscv32

import chisel3._

class ALU extends  Module{
    val io = IO(new Bundle{
        val pc   = Input(UInt(32.W))
        val src1 = Input(UInt(32.W))
        val src2 = Input(UInt(32.W))
        val alu_ctrl = Input(UInt(5.W))
        val alu_result = Output(UInt(32.W))
    })
    io.alu_result := MuxLookup(io.alu_ctrl, 0.U,Array(
        1.U -> src1 + src2
    ))


}