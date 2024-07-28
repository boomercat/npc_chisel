
import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import ctrlwire._

class IDUIO extends  Bundle{
    val instruction = Input(UInt(32.W))
    val imm         = Output(UInt(32.W))
    val regAdd      = new RegAddBundle()
    val bundleCtrl  = new BundleContrl()

}

class IDU extends Module {
  val io  = IO(new IDUIO())
  val imm = WireDefault(0.U(32.W))  
  
  
  io.regAdd.rs1 := io.instruction(19,15)
  io.regAdd.rs2 := io.instruction(24,20)
  io.regAdd.rd  := io.instruction(11,7)




  val imm_i = Cat(Fill(20, io.inst(31)), io.inst(31, 20))
  val imm_s = Cat(Fill(20, io.inst(31)), io.inst(31, 25), io.inst(11, 7))
  val imm_b = Cat(Fill(20, io.inst(31)), io.inst(7), io.inst(30, 25), io.inst(11, 8), 0.U(1.W))
  val imm_u = Cat(io.inst(31, 12), Fill(12, 0.U))
  val imm_j = Cat(Fill(12, io.inst(31)), io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), Fill(1, 0.U))
  
  io.ctrl.ext_type := controlValues(0)

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


  val default = List(0.U(3.W), 0.U(5.W), 0.U(1.W))
  // 定义解码表
  val decodeTable = Seq(
    // {imm_type(3), alu_ctrl(5), reg_write(1)}
    BitPat("b????????????????????_?????_0010111") -> List(3.U(3.W), 1.U(5.W), 1.U(1.W))
    // 在这里添加更多解码表项
  )


  // 根据指令解码
  val controlValues = ListLookup(io.instruction, default, decodeTable)

  io.bundleCtrl.alu_ctrl_op     := controlValues(1)
  io.bundleCtrl.reg_ctrl_write  := controlValus(2)

  io.imm := imm
}
