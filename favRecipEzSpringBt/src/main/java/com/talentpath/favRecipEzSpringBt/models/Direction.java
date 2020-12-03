package com.talentpath.favRecipEzSpringBt.models;

//@Entity
//@Table(name="Dirs")
public class Direction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


//    @NotBlank
    String instructionLine;


//    @ManyToOne(fetch=FetchType.EAGER)
//    @JoinColumn(name="recipe_id")
//    Recipe parentRecipe;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstructionLine() {
        return instructionLine;
    }

    public void setInstructionLine(String instructionLine) {
        this.instructionLine = instructionLine;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Direction direction = (Direction) o;

        if (id != direction.id) return false;
        return instructionLine != null ? instructionLine.equals(direction.instructionLine) : direction.instructionLine == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (instructionLine != null ? instructionLine.hashCode() : 0);
        return result;
    }

}

