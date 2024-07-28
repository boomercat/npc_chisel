package riscv32

import chisel3._
import chisel3.util._

import ctrlwire._

class ControlIO extends Bundle{
    val bundleCtrlIn     = Flipped(new BundleCtrl())
    val bundleAluctrl    = Flipped(new BundleAlu())
    val bundleControlOut = new BundleCtrl()

}

class Controller extends  Module{
    val io = IO (new ControlIO)
    
    io.bundleAluctrl.alu_ctrl_op := io.bundleCtrlIn.alu_ctrl_op

    io.bundleControlOut <> io.bundleCtrlIn
}