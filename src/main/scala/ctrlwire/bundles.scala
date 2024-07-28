package ctrlwire

import chisel3._


class BundleCtrl extends  Bundle{
    val alu_ctrl_op    = Output(UInt(5.W))
    val reg_ctrl_write = Output(UInt(5.W))

}


class BundleAlu extends Bundle{
    val alu_ctrl_op     = Input(UInt(5.W))

}

class BundleReg extends  Bundle{
    val rs1 = Output(UInt(5.W))
    val rs2 = Output(UInt(5.W))
    val rd  = Output(UInt(5.W))
}