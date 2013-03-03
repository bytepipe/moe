package org.moe.runtime.nativeobjects

import scala.collection.mutable.HashMap
import org.moe.runtime._
import org.moe.runtime.nativeobjects._

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class MoeHashObjectTestSuite extends FunSuite with BeforeAndAfter {

  var r : MoeRuntime = _

  before {
    r = new MoeRuntime()
    r.bootstrap()
  }

  test("... simple Hash object") {
    val o = new MoeHashObject(
      HashMap(
        "foo" -> new MoeUndefObject(),
        "bar" -> new MoeIntObject(10)
      )
    )
    val hash = o.getNativeValue
    assert(hash("foo").isUndef)
    assert(hash("bar").unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }

  test("... simple Hash object with class") {
    val c = new MoeClass("Hash")
    val o = new MoeHashObject(HashMap(), Some(c))
    assert(o.getAssociatedClass.get === c)
  }

  test("... false Hash object") {
    val o = new MoeHashObject(HashMap())
    assert(!o.isTrue)
    assert(o.isFalse)
    assert(!o.isUndef)
  }

  test("... complex Hash object") {
    val o = new MoeHashObject(
      HashMap(
        "foo" -> new MoeArrayObject(
          List(
            new MoeUndefObject(),
            new MoeIntObject(10)
          )
        ),
        "bar" -> new MoeIntObject(10)
        )
    )
    val hash = o.getNativeValue
    assert(hash("bar").unboxToInt.get === 10)
    val nested = hash("foo").unboxToList.get
    assert(nested(0).isUndef)
    assert(nested(1).unboxToInt.get === 10)
    assert(o.isTrue)
    assert(!o.isFalse)
    assert(!o.isUndef)
  }
  
  test("... keys") {
    val o = new MoeHashObject(
      HashMap(
        "foo" -> new MoeIntObject(20),
        "bar" -> new MoeIntObject(10)
        )
    )
    val x = o.keys(r)
    assert(x.isInstanceOf("Array"))
    assert(x.at_pos(r, new MoeIntObject(0)).unboxToString.get === "foo")
    assert(x.at_pos(r, new MoeIntObject(1)).unboxToString.get === "bar")
  }

  test("... values") {
    val o = new MoeHashObject(
      HashMap(
        "foo" -> new MoeIntObject(20),
        "bar" -> new MoeIntObject(10)
        )
    )
    val x = o.values(r)
    assert(x.isInstanceOf("Array"))
    assert(x.at_pos(r, new MoeIntObject(0)).unboxToInt.get === 20)
    assert(x.at_pos(r, new MoeIntObject(1)).unboxToInt.get === 10)
  }

}
