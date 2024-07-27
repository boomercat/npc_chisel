package  riscv32

import chisel3._
class  Top extends  Module{
    val io = IO(new Bundle{
    val pc  = Input(UInt(32.W))
    val imm = Output(UInt(32.W))        
    
    })
    val idu = Module(new IDU)
}