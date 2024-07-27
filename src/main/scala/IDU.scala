
import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import ctrlwire._

class IDUIO extends  Bundle{
    val instruction = Input(UInt(32.W))
    val imm         = Output(UInt(32.W))
    val regAdd      = new RegAddBundle()

}

class IDU extends Module {
  val io = IO(new IDUIO())
  io.regAdd.rs1 := io.instruction(19,15)
  io.regAdd.rs2 := io.instruction(24,20)
  io.regAdd.rd  := io.instruction(11,7)




  val imm_i = Cat(Fill(20, io.inst(31)), io.inst(31, 20))
  val imm_s = Cat(Fill(20, io.inst(31)), io.inst(31, 25), io.inst(11, 7))
  val imm_b = Cat(Fill(20, io.inst(31)), io.inst(7), io.inst(30, 25), io.inst(11, 8), 0.U(1.W))
  val imm_u = Cat(io.inst(31, 12), Fill(12, 0.U))
  val imm_j = Cat(Fill(12, io.inst(31)), io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), Fill(1, 0.U))

  when(imm_type === 1.U){
    imm := imm_s
  }.elsewhen(imm_type === 2.U){
    imm := imm_b
  }.elsewhen(imm_type === 3.U){
    imm := imm_u
  }.elsewhen(imm_type === 4.U){
    imm := imm_j
  }.otherwise{
    imm := imm_i
  }
  // 定义解码表
  val decodeTable = Seq(
    // {imm_type(3), alu_ctrl(5), reg_write(1), rd_add}
    BitPat("b????????????????????_?????_0010111") -> List(1.U(3.W), 1.U(5.W), 1.U(1.W), 1.U(1.W)),
    // 在这里添加更多解码表项
  )

  val default = List(0.U(3.W), 0.U(5.W), false.B, 0.U(5.W))

  // 根据指令解码
  val controlValues = ListLookup(io.instruction, default, decodeTable)

  io.ctrl.ext_type := controlValues(0)
  io.ctrl.alu_ctrl := controlValues(1)
  io.ctrl.reg_write := controlValues(2)
  io.ctrl.rd_add := controlValues(3)

  val extImmval1 = Module(new Ext_imm)
  extImmval1.io.imm_type := io.ctrl.ext_type
  extImmval1.io.instruction := io.instruction
  io.imm := extImmval1.io.imm
}
