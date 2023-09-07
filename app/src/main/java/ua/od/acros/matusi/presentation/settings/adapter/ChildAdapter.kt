package ua.od.acros.matusi.presentation.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding4.view.clicks
import ua.od.acros.matusi.R
import ua.od.acros.matusi.domain.model.Child
import ua.od.acros.matusi.databinding.ChildItemBinding
import ua.od.acros.matusi.presentation.misc.generateId

class ChildAdapter(
    private val onAddChild: (Child) -> Unit,
    private val onRemoveChild: (String) -> Unit
): RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    var children: ArrayList<Child> = arrayListOf()
        get() = ArrayList(field)
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChildItemBinding.inflate(inflater, parent, false)
        return ChildViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val child = children[position]
        with(holder) {
            currentPosition = position

            etName.setText(child.name)
            val age = child.age
            var text = ""
            if (age > 0) {
                collapsed = false
                etAge.setText(age.toString())
                text = "${child.name}, ${child.age}"
            } else {
                collapsed = true
                etAge.setText("")
                text = holder.itemView.context.getString(R.string.kid, position + 1)
            }
            childId = child.id
            if (collapsed) {
                tvKidDesc.text = holder.itemView.context.getString(R.string.kid, position + 1)
                groupEditText.isVisible = true
                btnOk.isVisible = true
                groupEditDelete.isGone = true
                groupAddChild.isGone = true
            } else {
                tvKidDesc.text = text
                groupEditText.isGone = true
                btnOk.isGone = true
                groupEditDelete.isVisible = true
                if (position == children.size - 1)
                    groupAddChild.isVisible = true
            }
        }
    }

    inner class ChildViewHolder(binding: ChildItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {
        val etName = binding.etEnterChildName
        val etAge = binding.etEnterChildAge

        val tvKidDesc = binding.tvKid

        val groupEditDelete = binding.groupEditDelete
        val groupEditText = binding.groupEditText
        val groupAddChild = binding.groupAddChild

        var childId: String = ""

        val btnOk = binding.btnOk
        private val btnDelete = binding.btnDelete
        private val btnEdit = binding.btnEdit
        private val btnAdd = binding.btnAdd

        var collapsed = false
        var currentPosition = -1

        init {
            btnEdit.clicks().subscribe {
                if (!collapsed) {
                    groupEditText.isVisible = true
                    btnOk.isVisible = true
                    groupEditDelete.isGone = true
                    collapsed = true
                }
            }
            btnDelete.clicks().subscribe {
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.delete_child_title)
                    .setMessage(R.string.delete_child_body)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { _, _ ->
                        onRemoveChild(childId)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()

            }

            btnOk.clicks().subscribe {
                val childName = etName.text.toString()
                val childAge = etAge.text.toString()
                if (childName != "" && childAge != "") {
                    groupEditText.isGone = true
                    btnOk.isGone = true
                    groupEditDelete.isVisible = true
                    collapsed = false
                    val kidsDesc = "$childName, $childAge"
                    tvKidDesc.text = kidsDesc
                    val child = Child(
                        childId,
                        childName,
                        childAge.toLong()
                    )
                    onAddChild(child)

                    if (currentPosition == children.size - 1)
                        groupAddChild.isVisible = true
                }
            }

            btnAdd.clicks().subscribe {
                groupAddChild.isGone = true
                val temp = ArrayList(children)
                temp.add(Child(generateId(), "", -1))
                children = temp
            }
        }
    }

    override fun getItemCount() = children.size
}