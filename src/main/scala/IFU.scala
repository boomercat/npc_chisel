import chisel3._
import chisel3.util._

class IFU extends Module {
  val io = IO(new Bundle {
    val pc = Input(UInt(32.W))
    val instruction = Output(UInt(32.W))
  })

  // 实例化BlackBox模块
  val pmem = Module(new PmemBlackBox)

  // 连接时钟和复位
  pmem.io.clock := clock
  pmem.io.reset := reset

  // 连接输入
  pmem.io.pc := io.pc

  // 连接输出
  io.instruction := pmem.io.instruction
}
