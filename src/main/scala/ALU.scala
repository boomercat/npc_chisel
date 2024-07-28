package riscv32

import chisel3._
import chisel3.util._

import config.Configs._
import ctrlwire._

class AluIO  extends Bundle{
    val BundleControl = new BundleControl()
    // val rs1              = Input(UInt(32.W))
    // val rs2              = Input(UInt(32.W))
    val imm              = Input(UInt(32.W))
    val pc               = Input(UInt(32.W))
    val alu_result       = Output(UInt(32.W))
    // val pc_jump          = Output(UInt(32.W))
}
class ALU extends  Module{
    val io = (new AluIO())

    val alu_result = WireDefault(0.U(32.W))

    switch(io.BundleControl.alu_ctrl_op){
        is(alu_nop){ alu_result := 0.U}
        is(alu_add){ alu_result := io.pc + io.imm}
    }


    io.alu_result := alu_result


}